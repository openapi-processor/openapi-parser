/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.maps.shouldContainExactly
import io.kotest.matchers.maps.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.openapiparser.Converter
import io.openapiparser.ConverterException
import io.openapiparser.Reader
import io.openapiparser.jackson.JacksonConverter
import io.openapiparser.memory.Memory
import io.openapiparser.reader.StringReader
import io.openapiparser.reader.UriReader
import java.io.IOException
import java.net.URI

class ResolverSpec: StringSpec({

    afterEach {
        Memory.clear()
    }

    "resolves schema" {
        val store = DocumentStore()
        val resolver = Resolver(StringReader(""), JacksonConverter(), store)

        val uri = URI.create("https://document")
        val doc = emptyMap<String, Any>()
        val result = resolver.resolve(uri, doc)

        result.uri shouldBe uri
        result.document shouldBe doc
        result.registry.shouldNotBeNull()
    }

    "resolves schema with internal ref" {
        val store = DocumentStore()
        val content = """
            ${'$'}ref: '#/definitions/foo'
            definitions:
              foo: {}
        """.trimIndent()
        val resolver = Resolver(StringReader(content), JacksonConverter(), store)

        val uri = URI.create("https://document")
        val result = resolver.resolve(uri)

        result.uri shouldBe uri
        val doc = result.document as Map<String, Any>
        doc.shouldHaveSize(2)
        val ref = result.registry.getRef(URI.create("https://document#/definitions/foo"))
        ref.rawValue.shouldNotBeNull()
    }

    "throws if reading baseUri fails" {
        val reader = mockk<Reader>()
        every { reader.read(any()) } throws IOException()

        shouldThrow<ResolverException> {
            val resolver = Resolver(UriReader(), mockk(), mockk())
            resolver.resolve(URI("https:/fails/to/load/openapi.yaml"))
        }
    }

    "throws if conversion fails" {
        val converter = mockk<Converter>()
        every { converter.convert(any()) } throws ConverterException("failed", Exception())

        shouldThrow<ResolverException> {
            val resolver = Resolver(StringReader("broken"), converter, mockk())
            resolver.resolve(URI("https:/fails/to/convert/openapi.yaml"))
        }
    }

    "resolves schema with external ref" {
        val instant = """
            ${'$'}ref: foo.yaml#/foo
            """.trimIndent()

        val foo = """
            foo: FOO!
            """.trimIndent()

        Memory.add("/instant.yaml", instant)
        Memory.add("/foo.yaml", foo)

        val store = DocumentStore()
        val resolver = Resolver(UriReader(), JacksonConverter(), store)

        val uri = URI("memory:/instant.yaml")
        val result = resolver.resolve(uri)

        val doc = result.document as Map<String, Any>
        doc.shouldContainExactly(mapOf("${'$'}ref" to "foo.yaml#/foo"))
        result.uri shouldBe uri

        val ref = result.registry.getRef(URI.create("memory:/foo.yaml#/foo"))
        ref.rawValue.shouldNotBeNull()
    }

    "throws if resolving of external ref fails" {
        val instant = """
            ${'$'}ref: foo.yaml#/foo
            """.trimIndent()

        Memory.add("/instant.yaml", instant)

        val store = DocumentStore()
        val resolver = Resolver(UriReader(), JacksonConverter(), store)

        val uri = URI("memory:/instant.yaml")
        shouldThrow<ResolverException> {
            resolver.resolve(uri)
        }
    }

})

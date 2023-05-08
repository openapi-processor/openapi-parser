/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.schema

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.maps.shouldContainExactly
import io.kotest.matchers.maps.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.openapiprocessor.interfaces.Converter
import io.openapiprocessor.interfaces.ConverterException
import io.openapiprocessor.interfaces.Reader
import io.openapiprocessor.jsonschema.converter.Types.asMap
import io.openapiparser.jackson.JacksonConverter
import io.openapiparser.memory.Memory
import io.openapiprocessor.jsonschema.reader.StringReader
import io.openapiprocessor.jsonschema.reader.UriReader
import java.io.IOException
import java.net.URI

class ResolverSpec: StringSpec({

    afterEach {
        Memory.clear()
    }

    "resolves schema" {
        val store = DocumentStore()
        val loader = DocumentLoader(
            StringReader(""), JacksonConverter()
        )
        val settings = Resolver.Settings (SchemaVersion.Draft4)
        val resolver = Resolver(store, loader, settings)

        val uri = URI.create("https://document")
        val doc = emptyMap<String, Any>()
        val result = resolver.resolve(uri, doc)

        result.scope.documentUri shouldBe uri
        result.scope.baseUri shouldBe uri
        result.scope.version shouldBe settings.version
        result.document shouldBe doc
        result.registry.shouldNotBeNull()
    }

    "resolves schema with internal ref" {
        val content = """
            ${'$'}ref: '#/definitions/foo'
            definitions:
              foo: {}
        """.trimIndent()

        val store = DocumentStore()
        val loader = DocumentLoader(
            StringReader(content), JacksonConverter()
        )
        val settings = Resolver.Settings (SchemaVersion.Draft4)
        val resolver = Resolver(store, loader, settings)

        val uri = URI.create("https://document")
        val result = resolver.resolve(uri)

        result.scope.documentUri shouldBe uri
        result.scope.baseUri shouldBe uri
        result.scope.version shouldBe settings.version

        val doc = asMap(result.document)
        doc.shouldHaveSize(2)

        val ref = result.registry.getReference(URI.create("https://document#/definitions/foo"))
        ref.rawValue.shouldNotBeNull()
    }

    // mockk fails
    "throws if reading baseUri fails".config(enabled = false) {
        val reader = mockk<Reader>()
        every { reader.read(any()) } throws IOException()

        val store = DocumentStore()
        val loader = DocumentLoader(reader, mockk())
        val settings = Resolver.Settings (SchemaVersion.Draft4)
        val resolver = Resolver(store, loader, settings)

        shouldThrow<ResolverException> {
            resolver.resolve(URI("https:/fails/to/load/openapi.yaml"))
        }
    }

    // mockk fails
    "throws if conversion fails".config(enabled = false) {
        val converter = mockk<Converter>()
        every { converter.convert(any()) } throws ConverterException(
            "failed",
            Exception()
        )

        val store = DocumentStore()
        val loader = DocumentLoader(
            StringReader("broken"), mockk()
        )
        val settings = Resolver.Settings (SchemaVersion.Draft4)
        val resolver = Resolver(store, loader, settings)

        shouldThrow<ResolverException> {
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
        val loader = DocumentLoader(
            UriReader(),
            JacksonConverter()
        )
        val settings = Resolver.Settings (SchemaVersion.Draft4)
        val resolver = Resolver(store, loader, settings)

        val uri = URI("memory:/instant.yaml")
        val result = resolver.resolve(uri)

        val doc = asMap(result.document)
        doc.shouldContainExactly(mapOf("${'$'}ref" to "foo.yaml#/foo"))
        result.scope.documentUri shouldBe uri
        result.scope.baseUri shouldBe uri
        result.scope.version shouldBe settings.version

        val ref = result.registry.getReference(URI.create("memory:/foo.yaml#/foo"))
        ref.rawValue.shouldNotBeNull()
    }

    "throws if resolving of external ref fails" {
        val instant = """
            ${'$'}ref: foo.yaml#/foo
            """.trimIndent()

        Memory.add("/instant.yaml", instant)

        val store = DocumentStore()
        val loader = DocumentLoader(
            UriReader(),
            JacksonConverter()
        )
        val settings = Resolver.Settings (SchemaVersion.Draft4)
        val resolver = Resolver(store, loader, settings)

        shouldThrow<ResolverException> {
            resolver.resolve(URI("memory:/instant.yaml"))
        }
    }
})

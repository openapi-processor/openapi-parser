/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.openapiparser.jackson.JacksonConverter
import io.openapiparser.support.StringReader
import java.io.IOException
import java.net.URI

class ReferenceResolverSpec : StringSpec({

    "reads OpenAPI document" {
        val uri = URI("file:///any")
        val resolver = ReferenceResolver(
            uri,
            StringReader("""
                openapi: 3.0.3
            """.trimIndent()),
            JacksonConverter(),
            ReferenceRegistry(uri)
        )

        resolver.resolve()

        val node = resolver.baseNode
        node.countProperties shouldBe 1
        node["openapi"] shouldBe "3.0.3"
    }

    "throws if reading fails" {
        val reader = mockk<Reader>()
        every { reader.read(any()) } throws IOException()

        shouldThrow<ResolverException> {
            val resolver = ReferenceResolver(null, reader, null, null)
            resolver.resolve()
        }
    }

    "throws if conversion fails" {
        val converter = mockk<Converter>()
        every { converter.convert(any()) } throws ConverterException("failed", null)

        shouldThrow<ResolverException> {
            val resolver = ReferenceResolver(
                null,
                StringReader("openapi: 3.0.3"),
                converter,
                null)
            resolver.resolve()
        }
    }

})

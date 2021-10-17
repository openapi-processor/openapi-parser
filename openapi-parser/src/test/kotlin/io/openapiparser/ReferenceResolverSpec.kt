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
        val resolver = ReferenceResolver(
            StringReader("""
                openapi: 3.0.3
            """.trimIndent()),
            JacksonConverter()
        )

        resolver.resolve(URI("file:///any"))

        val node = resolver.baseNode
        node.size shouldBe 1
        node["openapi"] shouldBe "3.0.3"
    }

    "throws if reading fails" {
        val reader = mockk<Reader>()
        every { reader.read(any()) } throws IOException()

        shouldThrow<ResolverException> {
            val resolver = ReferenceResolver(
                reader,
                null)
            resolver.resolve(URI("file:///any"))
        }
    }

    "throws if conversion fails" {
        val converter = mockk<Converter>()
        every { converter.convert(any()) } throws ConverterException("failed", null)

        shouldThrow<ResolverException> {
            val resolver = ReferenceResolver(
                StringReader("openapi: 3.0.3"),
                converter)
            resolver.resolve(URI("file:///any"))
        }
    }

})

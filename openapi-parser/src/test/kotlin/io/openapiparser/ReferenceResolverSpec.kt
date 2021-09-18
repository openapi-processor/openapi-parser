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
            URI("file:///any"),
            StringReader("""
                openapi: 3.0.3
            """.trimIndent()),
            JacksonConverter(),
            null
        )

        resolver.resolve()

        val node = resolver.baseNode
        node.size shouldBe 1
        node["openapi"] shouldBe "3.0.3"
    }

    "throws if reading fails" {
        val reader = mockk<Reader>()
        every { reader.read(any()) } throws IOException()

        shouldThrow<ResolverException> {
            val resolver = ReferenceResolver(
                null,
                reader,
                null,
                null)
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

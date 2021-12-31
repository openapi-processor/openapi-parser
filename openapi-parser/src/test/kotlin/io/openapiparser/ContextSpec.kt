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
import io.openapiparser.reader.StringReader
import java.lang.Exception
import java.net.URI

class ContextSpec : StringSpec({

    "reads OpenAPI document" {
        val uri = URI("file:///any")
        val ctx = Context(
            uri,
            ReferenceResolver(
                uri,
                StringReader("""
                    openapi: 3.0.3
                    """.trimIndent()),
                JacksonConverter(),
                ReferenceRegistry()
            )
        )

        ctx.read()

        val bucket = ctx.`object`
        bucket.size shouldBe 1
        bucket.getRawValue("openapi") shouldBe "3.0.3"
    }

    "throws if reading fails" {
        val resolver = mockk<ReferenceResolver>()
        every { resolver.resolve() } throws ResolverException("failed", Exception())

        shouldThrow<ContextException> {
            val ctx = Context(
                URI("file:///any"),
                resolver)
            ctx.read()
        }
    }

})

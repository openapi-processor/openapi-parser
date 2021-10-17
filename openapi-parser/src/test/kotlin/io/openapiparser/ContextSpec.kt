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
import java.net.URI

class ContextSpec : StringSpec({

    "reads OpenAPI document" {
        val ctx = Context(
            URI("file:///any"),
            ReferenceResolver(
                StringReader("""
                    openapi: 3.0.3
                    """.trimIndent()),
                JacksonConverter())
        )

        ctx.read()

        val node = ctx.baseNode
        node.size shouldBe 1
        node["openapi"] shouldBe "3.0.3"
    }

    "throws if reading fails" {
        val resolver = mockk<ReferenceResolver>()
        every { resolver.resolve(any()) } throws ResolverException("failed", null)

        shouldThrow<ContextException> {
            val ctx = Context(
                URI("file:///any"),
                resolver)
            ctx.read()
        }
    }

})

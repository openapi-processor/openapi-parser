/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser

import io.kotest.core.spec.style.StringSpec

class ContextSpec : StringSpec({

//    "reads OpenAPI document" {
//        val uri = URI("file:///any")
//        val ctx = Context(
//            uri,
//            ReferenceResolver(
//                uri,
//                StringReader("""
//                    openapi: 3.0.3
//                    """.trimIndent()),
//                JacksonConverter(),
//                ReferenceRegistry()
//            )
//        )
//
//        val bucket = ctx.read()
//        bucket.rawValues.size shouldBe 1
//        bucket.getRawValue("openapi") shouldBe "3.0.3"
//    }
//
//    "throws if reading fails" {
//        val resolver = mockk<ReferenceResolver>()
//        every { resolver.resolve() } throws ResolverException("failed", Exception())
//
//        shouldThrow<ContextException> {
//            Context(URI("file:///any"), resolver).read()
//        }
//    }
})

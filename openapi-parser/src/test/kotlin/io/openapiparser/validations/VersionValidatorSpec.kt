/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validations

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.openapiparser.Node
import io.openapiparser.ValidationContext
import java.net.URI

class VersionValidatorSpec : StringSpec({

    "validate 'openapi' value is a major.minor.patch version string" {
        val ctx = ValidationContext(URI("file:///any"))
        val version = VersionValidator()

        val messages = version.validate(ctx, Node(mapOf<String, Any>(
            "openapi" to "3.0.0"
        )))

        messages.isEmpty() shouldBe true
    }

    "fails to validate 'openapi' value if it is null" {
        val api = mapOf<String, Any?>(
            "openapi" to null
        )

        val ctx = ValidationContext(URI("file:///any"))
        val version = VersionValidator()
        val messages = version.validate(ctx, Node(api))

        messages.size shouldBe 1
        val message = messages.first()
        message.text shouldContain "'null'"
        message.path shouldBe "$.openapi"
    }

    "fails to validate 'openapi' value if it is not a major.minor.patch version string" {
        val api = mapOf(
            "openapi" to "bad"
        )

        val ctx = ValidationContext(URI("file:///any"))
        val version = VersionValidator()
        val messages = version.validate(ctx, Node(api))

        messages.size shouldBe 1
        val message = messages.first()
        message.text shouldContain "'bad'"
        message.path shouldBe "$.openapi"
    }

    "fails to validate 'openapi' value if it is not a string" {
        val api = mapOf(
            "openapi" to 5
        )

        val ctx = ValidationContext(URI("file:///any"))
        val version = VersionValidator()
        val messages = version.validate(ctx, Node(api))

        messages.size shouldBe 1
        val message = messages.first()
        message.text shouldContain "'5'"
        message.path shouldBe "$.openapi"
    }

})

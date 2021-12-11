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

class RequiredPropertiesValidatorSpec : StringSpec({

    "validates required properties of node" {
        val ctx = ValidationContext(URI("file:///any"))
        val required = RequiredPropertiesValidator(
            listOf(
                "foo",
                "bar"
            )
        )

        val messages = required.validate(
            ctx,
            Node("$", mapOf<String, Any>(
                "foo" to "required",
                "bar" to "required",
                "oof" to "any",
                "rab" to "any"
            ))
        )

        messages.isEmpty() shouldBe true
    }

    "detects missing required properties of node" {
        val ctx = ValidationContext(URI("file:///any"))
        val required = RequiredPropertiesValidator(
            listOf(
                "foo",
                "bar"
            )
        )

        val messages = required.validate(ctx, Node.empty())

        messages.size shouldBe 2
        val messageFoo = ArrayList(messages)[0]
        messageFoo.text shouldContain "'foo'"
        messageFoo.path shouldBe "$.foo"

        val messageBar = ArrayList(messages)[1]
        messageBar.text shouldContain "'bar'"
        messageBar.path shouldBe "$.bar"
    }

})

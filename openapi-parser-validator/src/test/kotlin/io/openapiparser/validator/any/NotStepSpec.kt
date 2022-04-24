/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.any

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import io.openapiparser.schema.*
import io.openapiparser.validator.ValidationMessage
import io.openapiparser.validator.support.TestStep
import java.net.URI

class NotStepSpec : StringSpec({

    val sctx = JsonSchemaContext(URI(""), ReferenceRegistry(), SchemaVersion.Default)
    val schema = JsonSchemaObject(mapOf(), sctx)

    val ictx = JsonInstanceContext(URI(""), ReferenceRegistry())
    val instance = JsonInstance("value", ictx)

    val error = ValidationMessage(schema, instance, "error")

    "step is valid if sub step is not" {
        val step = NotStep(schema, instance, TestStep(listOf(error)))
        step.isValid.shouldBeTrue()
        step.messages.isEmpty()
    }

    "step is invalid if sub step is valid" {
        val step = NotStep(schema, instance, TestStep(listOf()))

        step.isValid.shouldBeFalse()
        step.messages.size shouldBe 1
    }

    "invalid step reports no sub step errors" {
        val step = NotStep(schema, instance, TestStep(listOf()))

        step.isValid.shouldBeFalse()
        step.messages.first().nestedMessages.shouldBeEmpty()
    }
})

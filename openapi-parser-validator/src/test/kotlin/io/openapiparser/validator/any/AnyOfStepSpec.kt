/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.any

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.openapiparser.schema.*
import io.openapiparser.schema.UriSupport.emptyUri
import io.openapiparser.validator.ValidationMessage
import io.openapiparser.validator.support.TestStep

class AnyOfStepSpec : StringSpec({

    val schemaContext = JsonSchemaContext(
        Scope(emptyUri(), null, SchemaVersion.getLatest()),
        ReferenceRegistry())
    val schema = JsonSchemaObject(mapOf(), schemaContext)

    val instanceContext = JsonInstanceContext(
        Scope(emptyUri(), null, SchemaVersion.getLatest()),
        ReferenceRegistry())
    val instance = JsonInstance("value", instanceContext)

    val error = ValidationMessage(schema, instance, "anyOf", "error")

    "step is valid without error" {
        val step = AnyOfStep(schema, instance)
        step.isValid.shouldBeTrue()
        step.messages.isEmpty()
    }

    "step is invalid with error" {
        val step = AnyOfStep(schema, instance)
        step.setInvalid()

        step.isValid.shouldBeFalse()
        step.messages.size shouldBe 1
    }

    "invalid step reports all sub step errors" {
        val step = AnyOfStep(schema, instance)
        step.add(TestStep(listOf(error)))
        step.add(TestStep(listOf(error)))
        step.setInvalid()

        step.isValid.shouldBeFalse()
        step.messages.first().nestedMessages.size shouldBe 2
    }
})

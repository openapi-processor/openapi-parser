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
import io.openapiparser.schema.UriSupport.emptyUri
import io.openapiparser.validator.ValidationMessage
import io.openapiparser.validator.support.TestStep

class NotStepSpec : StringSpec({

    val schemaContext = JsonSchemaContext(
        Scope(emptyUri(), null, SchemaVersion.getLatest()), ReferenceRegistry())
    val schema = JsonSchemaObject(mapOf(), schemaContext)

    val instanceContext = JsonInstanceContext(
        Scope(emptyUri(), null, SchemaVersion.getLatest()), ReferenceRegistry())
    val instance = JsonInstance("value", instanceContext)

    val error = ValidationMessage(schema, instance, "not", "error")

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

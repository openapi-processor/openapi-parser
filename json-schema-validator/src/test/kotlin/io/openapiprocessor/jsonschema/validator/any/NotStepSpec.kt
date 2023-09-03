/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.any

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.types.shouldBeInstanceOf
import io.openapiprocessor.jsonschema.support.UriSupport.emptyUri
import io.openapiprocessor.jsonschema.validator.support.TestStep
import io.openapiprocessor.jsonschema.schema.*

class NotStepSpec : StringSpec({

    val schemaContext = JsonSchemaContext(
        Scope(
            emptyUri(),
            null,
            SchemaVersion.getLatest()
        ), ReferenceRegistry()
    )
    val schema = JsonSchemaObject(mapOf(), schemaContext)

    val instance = JsonInstance("value")

    "step is valid if sub step is not" {
        val step = NotStep(schema, instance)
        step.add(TestStep(valid = false))

        step.isValid.shouldBeTrue()
        step.message.shouldBeNull()
    }

    "step is invalid if sub step is valid" {
        val step = NotStep(schema, instance)
        step.add(TestStep(valid = true))

        step.isValid.shouldBeFalse()
        step.message.shouldNotBeNull()
    }

    "invalid step should report error" {
        val step = NotStep(schema, instance)
        step.add(TestStep(valid = true))

        step.message.shouldBeInstanceOf<NotError>()
    }
})

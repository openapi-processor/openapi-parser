/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.any

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.types.shouldBeInstanceOf
import io.openapiprocessor.jsonschema.validator.support.TestStep
import io.openapiprocessor.jsonschema.schema.*

class OneOfStepSpec : StringSpec({

    val schemaContext = JsonSchemaContext(
        Scope(
            UriSupport.emptyUri(),
            null,
            SchemaVersion.getLatest()
        ), ReferenceRegistry()
    )
    val schema = JsonSchemaObject(mapOf(), schemaContext)

    val instanceContext = JsonInstanceContext(
        Scope(
            UriSupport.emptyUri(),
            null,
            SchemaVersion.getLatest()
        ), ReferenceRegistry()
    )
    val instance = JsonInstance("value", instanceContext)

    "step is valid without sub step error" {
        val step = OneOfStep(schema, instance)
        step.add(TestStep(valid = true))

        step.isValid.shouldBeTrue()
        step.message.shouldBeNull()
    }

    "step is invalid with sub step error" {
        val step = OneOfStep(schema, instance)
        step.add(TestStep(valid = false))
        step.setInvalid()

        step.isValid.shouldBeFalse()
        step.message.shouldBeInstanceOf<OneOfError>()
    }
})

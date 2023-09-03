/*
 * Copyright 2023 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.steps

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.openapiprocessor.jsonschema.schema.JsonInstance
import io.openapiprocessor.jsonschema.schema.JsonPointer
import io.openapiprocessor.jsonschema.schema.JsonSchema
import io.openapiprocessor.jsonschema.support.UriSupport.createUri
import io.openapiprocessor.jsonschema.validator.ValidationMessage
import io.openapiprocessor.jsonschema.validator.support.createInstance
import io.openapiprocessor.jsonschema.validator.support.createSchema

class SimpleStepSpec : StringSpec({

    class TestSimpleStep(schema: JsonSchema, instance: JsonInstance, keyword: String):
        SimpleStep(schema, instance, keyword) {

        override fun getError(): ValidationMessage {
            return ValidationMessage(
                schema,
                instance,
                property,
                "error"
            )
        }
    }

    "schema & instance property" {
        val schema = createSchema(mapOf(
            "test" to mapOf<String, Any>()
        ), JsonPointer.empty(), "https://output.test/schema")

        val instance = createInstance(mapOf(
            "test" to "value"
        ), JsonPointer.empty())

        val step = TestSimpleStep(schema, instance, "test")

        step.instanceLocation shouldBe JsonPointer.from("")
        step.keywordLocation shouldBe JsonPointer.from("/test")
        step.absoluteKeywordLocation shouldBe createUri("https://output.test/schema#/test")
    }
})

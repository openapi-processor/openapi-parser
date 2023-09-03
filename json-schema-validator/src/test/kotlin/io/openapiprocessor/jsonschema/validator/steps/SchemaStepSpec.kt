/*
 * Copyright 2023 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.steps

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.openapiprocessor.jsonschema.schema.JsonPointer
import io.openapiprocessor.jsonschema.support.UriSupport.createUri
import io.openapiprocessor.jsonschema.validator.support.createInstance
import io.openapiprocessor.jsonschema.validator.support.createSchema

class SchemaStepSpec : StringSpec({

    "schema & instance empty" {
        val schema = createSchema(mapOf(), JsonPointer.empty(), "https://output.test/schema")
        val instance = createInstance(mapOf(), JsonPointer.empty())
        val step = SchemaStep(schema, instance)

        step.instanceLocation shouldBe JsonPointer.empty()
        step.keywordLocation shouldBe JsonPointer.empty()
        step.absoluteKeywordLocation shouldBe createUri("https://output.test/schema")
    }

    "schema property" {
        val schema = createSchema(mapOf(
            "foo" to mapOf<String, Any>()
        ), JsonPointer.from("/foo"), "https://output.test/schema")

        val instance = createInstance(mapOf(
            "foo" to "bar"
        ), JsonPointer.from("/foo"))
        val step = SchemaStep(schema, instance)

        step.instanceLocation shouldBe JsonPointer.from("/foo")
        step.keywordLocation shouldBe JsonPointer.from("/foo")
        step.absoluteKeywordLocation shouldBe createUri("https://output.test/schema#/foo")
    }
})

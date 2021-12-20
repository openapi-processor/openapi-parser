/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v3x

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.openapiparser.model.v30.schema as schema30
import io.openapiparser.model.v31.schema as schema31

class SchemaSpec: StringSpec({

    // json schema
    "gets schema multipleOf" {
        schema30("multipleOf: 9.9").multipleOf shouldBe 9.9
        schema31("multipleOf: 9.9").multipleOf shouldBe 9.9
    }

    "gets schema multipleOf is null if missing" {
        schema30().multipleOf.shouldBeNull()
        schema31().multipleOf.shouldBeNull()
    }

    "gets schema maximum" {
        schema30("maximum: 9.9").maximum shouldBe 9.9
        schema31("maximum: 9.9").maximum shouldBe 9.9
    }

    "gets schema maximum is null if missing" {
        schema30().maximum.shouldBeNull()
        schema31().maximum.shouldBeNull()
    }

    "gets schema exclusiveMaximum" {
        schema30("exclusiveMaximum: true").exclusiveMaximum.shouldBeTrue()
        schema31("exclusiveMaximum: true").exclusiveMaximum.shouldBeTrue()
    }

    "gets schema exclusiveMaximum is false if missing" {
        schema30().exclusiveMaximum.shouldBeFalse()
        schema31().exclusiveMaximum.shouldBeFalse()
    }

    "gets schema minimum" {
        schema30("minimum: 9.9").minimum shouldBe 9.9
        schema31("minimum: 9.9").minimum shouldBe 9.9
    }

    "gets schema minimum is null if missing" {
        schema30().minimum.shouldBeNull()
        schema31().minimum.shouldBeNull()
    }

    "gets schema exclusiveMinimum" {
        schema30("exclusiveMinimum: true").exclusiveMinimum.shouldBeTrue()
        schema31("exclusiveMinimum: true").exclusiveMinimum.shouldBeTrue()
    }

    "gets schema exclusiveMinimum is false if missing" {
        schema30().exclusiveMinimum.shouldBeFalse()
        schema31().exclusiveMinimum.shouldBeFalse()
    }

    "gets schema maxLength" {
        schema30("maxLength: 9").maxLength shouldBe 9
        schema31("maxLength: 9").maxLength shouldBe 9
    }

    "gets schema maxLength is null if missing" {
        schema30().maxLength.shouldBeNull()
        schema31().maxLength.shouldBeNull()
    }

    "gets schema minLength" {
        schema30("minLength: 9").minLength shouldBe 9
        schema31("minLength: 9").minLength shouldBe 9
    }

    "gets schema minLength is null if missing" {
        schema30().minLength.shouldBeNull()
        schema31().minLength.shouldBeNull()
    }

    "gets schema pattern" {
        schema30("pattern: regex").pattern shouldBe "regex"
        schema31("pattern: regex").pattern shouldBe "regex"
    }

    "gets schema pattern is null if missing" {
        schema30().pattern.shouldBeNull()
        schema31().pattern.shouldBeNull()
    }

    "gets schema minItems" {
        schema30("minItems: 9").minItems shouldBe 9
        schema31("minItems: 9").minItems shouldBe 9
    }

    "gets schema minItems is 0 if missing" {
        schema30().minItems shouldBe 0
        schema31().minItems shouldBe 0
    }

    "gets schema maxItems" {
        schema30("maxItems: 9").maxItems shouldBe 9
        schema31("maxItems: 9").maxItems shouldBe 9
    }

    "gets schema maxItems is null if missing" {
        schema30().maxItems.shouldBeNull()
        schema31().maxItems.shouldBeNull()
    }

    "gets schema uniqueItems" {
        schema30("uniqueItems: true").uniqueItems.shouldBeTrue()
        schema31("uniqueItems: true").uniqueItems.shouldBeTrue()
    }

    "gets schema uniqueItems is false if missing" {
        schema30().uniqueItems.shouldBeFalse()
        schema31().uniqueItems.shouldBeFalse()
    }

    "gets schema minProperties" {
        schema30("minProperties: 9").minProperties shouldBe 9
        schema31("minProperties: 9").minProperties shouldBe 9
    }

    "gets schema minProperties is 0 if missing" {
        schema30().minProperties shouldBe 0
        schema31().minProperties shouldBe 0
    }

    "gets schema maxProperties" {
        schema30("maxProperties: 9").maxProperties shouldBe 9
        schema31("maxProperties: 9").maxProperties shouldBe 9
    }

    "gets schema maxProperties is null if missing" {
        schema30().maxProperties.shouldBeNull()
        schema31().maxProperties.shouldBeNull()
    }

    "gets schema required" {
        schema30("required: [foo, bar]").required.shouldContainExactly(listOf("foo", "bar"))
        schema31("required: [foo, bar]").required.shouldContainExactly(listOf("foo", "bar"))
    }

    "gets schema required is empty if missing" {
        schema30().required.shouldBeEmpty()
        schema31().required.shouldBeEmpty()
    }

    "gets schema format" {
        schema30("format: foo").format shouldBe "foo"
        schema31("format: foo").format shouldBe "foo"
    }

    "gets schema format is null if missing" {
        schema30().format.shouldBeNull()
        schema31().format.shouldBeNull()
    }

    "gets schema enum" {
        schema30("enum: [foo, bar]").enum shouldBe listOf("foo", "bar")
        schema31("enum: [foo, bar]").enum shouldBe listOf("foo", "bar")
    }

    "gets schema enum is null if missing" {
        schema30().enum.shouldBeNull()
        schema31().enum.shouldBeNull()
    }

    include(testExtensions("schema 30", ::schema30) { it.extensions })
    include(testExtensions("schema 31", ::schema31) { it.extensions })
})

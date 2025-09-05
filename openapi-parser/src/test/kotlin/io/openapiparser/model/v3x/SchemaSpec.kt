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
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.openapiparser.model.v30.schema as schema30
import io.openapiparser.model.v31.schema as schema31
import io.openapiparser.model.v32.schema as schema32

class SchemaSpec: StringSpec({

    // json schema
    "gets schema multipleOf" {
        schema30("multipleOf: 9.9").multipleOf shouldBe 9.9
        schema31("multipleOf: 9.9").multipleOf shouldBe 9.9
        schema32("multipleOf: 9.9").multipleOf shouldBe 9.9
    }

    "gets schema multipleOf is null if missing" {
        schema30().multipleOf.shouldBeNull()
        schema31().multipleOf.shouldBeNull()
        schema32().multipleOf.shouldBeNull()
    }

    "gets schema maximum" {
        schema30("maximum: 9.9").maximum shouldBe 9.9
        schema31("maximum: 9.9").maximum shouldBe 9.9
        schema32("maximum: 9.9").maximum shouldBe 9.9
    }

    "gets schema maximum is null if missing" {
        schema30().maximum.shouldBeNull()
        schema31().maximum.shouldBeNull()
        schema32().maximum.shouldBeNull()
    }

    "gets schema exclusiveMaximum" {
        schema30("exclusiveMaximum: true").exclusiveMaximum.shouldBeTrue()
        schema31("exclusiveMaximum: 9.9").exclusiveMaximum shouldBe 9.9
        schema32("exclusiveMaximum: 9.9").exclusiveMaximum shouldBe 9.9
    }

    "gets schema minimum" {
        schema30("minimum: 9.9").minimum shouldBe 9.9
        schema31("minimum: 9.9").minimum shouldBe 9.9
        schema32("minimum: 9.9").minimum shouldBe 9.9
    }

    "gets schema minimum is null if missing" {
        schema30().minimum.shouldBeNull()
        schema31().minimum.shouldBeNull()
        schema32().minimum.shouldBeNull()
    }

    "gets schema exclusiveMinimum" {
        schema30("exclusiveMinimum: true").exclusiveMinimum.shouldBeTrue()
        schema31("exclusiveMinimum: 9.9").exclusiveMinimum shouldBe 9.9
        schema32("exclusiveMinimum: 9.9").exclusiveMinimum shouldBe 9.9
    }

    "gets schema maxLength" {
        schema30("maxLength: 9").maxLength shouldBe 9
        schema31("maxLength: 9").maxLength shouldBe 9
        schema32("maxLength: 9").maxLength shouldBe 9
    }

    "gets schema maxLength is null if missing" {
        schema30().maxLength.shouldBeNull()
        schema31().maxLength.shouldBeNull()
        schema32().maxLength.shouldBeNull()
    }

    "gets schema minLength" {
        schema30("minLength: 9").minLength shouldBe 9
        schema31("minLength: 9").minLength shouldBe 9
        schema32("minLength: 9").minLength shouldBe 9
    }

    "gets schema minLength is null if missing" {
        schema30().minLength.shouldBeNull()
        schema31().minLength.shouldBeNull()
        schema32().minLength.shouldBeNull()
    }

    "gets schema pattern" {
        schema30("pattern: regex").pattern shouldBe "regex"
        schema31("pattern: regex").pattern shouldBe "regex"
        schema32("pattern: regex").pattern shouldBe "regex"
    }

    "gets schema pattern is null if missing" {
        schema30().pattern.shouldBeNull()
        schema31().pattern.shouldBeNull()
        schema32().pattern.shouldBeNull()
    }

    "gets schema minItems" {
        schema30("minItems: 9").minItems shouldBe 9
        schema31("minItems: 9").minItems shouldBe 9
        schema32("minItems: 9").minItems shouldBe 9
    }

    "gets schema minItems is 0 if missing" {
        schema30().minItems shouldBe 0
        schema31().minItems shouldBe 0
        schema32().minItems shouldBe 0
    }

    "gets schema maxItems" {
        schema30("maxItems: 9").maxItems shouldBe 9
        schema31("maxItems: 9").maxItems shouldBe 9
        schema32("maxItems: 9").maxItems shouldBe 9
    }

    "gets schema maxItems is null if missing" {
        schema30().maxItems.shouldBeNull()
        schema31().maxItems.shouldBeNull()
        schema32().maxItems.shouldBeNull()
    }

    "gets schema uniqueItems" {
        schema30("uniqueItems: true").uniqueItems.shouldBeTrue()
        schema31("uniqueItems: true").uniqueItems.shouldBeTrue()
        schema32("uniqueItems: true").uniqueItems.shouldBeTrue()
    }

    "gets schema uniqueItems is false if missing" {
        schema30().uniqueItems.shouldBeFalse()
        schema31().uniqueItems.shouldBeFalse()
        schema32().uniqueItems.shouldBeFalse()
    }

    "gets schema minProperties" {
        schema30("minProperties: 9").minProperties shouldBe 9
        schema31("minProperties: 9").minProperties shouldBe 9
        schema32("minProperties: 9").minProperties shouldBe 9
    }

    "gets schema minProperties is 0 if missing" {
        schema30().minProperties shouldBe 0
        schema31().minProperties shouldBe 0
        schema32().minProperties shouldBe 0
    }

    "gets schema maxProperties" {
        schema30("maxProperties: 9").maxProperties shouldBe 9
        schema31("maxProperties: 9").maxProperties shouldBe 9
        schema32("maxProperties: 9").maxProperties shouldBe 9
    }

    "gets schema maxProperties is null if missing" {
        schema30().maxProperties.shouldBeNull()
        schema31().maxProperties.shouldBeNull()
        schema32().maxProperties.shouldBeNull()
    }

    "gets schema required" {
        schema30("required: [foo, bar]").required.shouldContainExactly(listOf("foo", "bar"))
        schema31("required: [foo, bar]").required.shouldContainExactly(listOf("foo", "bar"))
        schema32("required: [foo, bar]").required.shouldContainExactly(listOf("foo", "bar"))
    }

    "gets schema required is empty if missing" {
        schema30().required.shouldBeEmpty()
        schema31().required.shouldBeEmpty()
        schema32().required.shouldBeEmpty()
    }

    "gets schema format" {
        schema30("format: foo").format shouldBe "foo"
        schema31("format: foo").format shouldBe "foo"
        schema32("format: foo").format shouldBe "foo"
    }

    "gets schema format is null if missing" {
        schema30().format.shouldBeNull()
        schema31().format.shouldBeNull()
        schema32().format.shouldBeNull()
    }

    "gets schema enum" {
        schema30("enum: [foo, bar]").enum shouldBe listOf("foo", "bar")
        schema31("enum: [foo, bar]").enum shouldBe listOf("foo", "bar")
        schema32("enum: [foo, bar]").enum shouldBe listOf("foo", "bar")
    }

    "gets schema enum is null if missing" {
        schema30().enum.shouldBeNull()
        schema31().enum.shouldBeNull()
        schema32().enum.shouldBeNull()
    }

    "gets schema title" {
        schema30("title: foo").title shouldBe "foo"
        schema31("title: foo").title shouldBe "foo"
        schema32("title: foo").title shouldBe "foo"
    }

    "gets schema title is null if missing" {
        schema30().title.shouldBeNull()
        schema31().title.shouldBeNull()
        schema32().title.shouldBeNull()
    }

    include(testDescription("schema 30", ::schema30) { it.description })
    include(testDescription("schema 31", ::schema31) { it.description })
    include(testDescription("schema 32", ::schema31) { it.description })

    "gets schema default" {
        schema30("default: {}").default.shouldNotBeNull()
        schema31("default: {}").default.shouldNotBeNull()
        schema32("default: {}").default.shouldNotBeNull()
    }

    "gets schema default is null if missing" {
        schema30().default.shouldBeNull()
        schema31().default.shouldBeNull()
        schema32().default.shouldBeNull()
    }

    "gets schema deprecated" {
        schema30("deprecated: true").deprecated.shouldBeTrue()
        schema31("deprecated: true").deprecated.shouldBeTrue()
        schema32("deprecated: true").deprecated.shouldBeTrue()
    }

    "gets schema deprecated is false if missing" {
        schema30().deprecated.shouldBeFalse()
        schema31().deprecated.shouldBeFalse()
        schema32().deprecated.shouldBeFalse()
    }

    "gets schema properties" {
        val source = """
          properties:
            foo: {}
            bar: {}
        """

        val p30 = schema30(source).properties
        p30.size shouldBe 2
        p30["foo"].shouldNotBeNull()
        p30["bar"].shouldNotBeNull()

        val p31 = schema31(source).properties
        p31.size shouldBe 2
        p31["foo"].shouldNotBeNull()
        p31["bar"].shouldNotBeNull()

        val p32 = schema32(source).properties
        p32.size shouldBe 2
        p32["foo"].shouldNotBeNull()
        p32["bar"].shouldNotBeNull()
    }

    "gets schema properties is empty if missing" {
        schema30().properties.shouldBeEmpty()
        schema31().properties.shouldBeEmpty()
        schema32().properties.shouldBeEmpty()
    }

    "gets schema items" {
        schema30("items: {}").items.shouldNotBeNull()
        schema31("items: {}").items.shouldNotBeNull()
        schema32("items: {}").items.shouldNotBeNull()
    }

    "gets schema items is null if missing" {
        schema30().items.shouldBeNull()
        schema31().items.shouldBeNull()
        schema32().items.shouldBeNull()
    }

    "gets schema allOf" {
        schema30("allOf: [{}]").allOf.size shouldBe 1
        schema31("allOf: [{}]").allOf.size shouldBe 1
        schema32("allOf: [{}]").allOf.size shouldBe 1
    }

    "gets schema allOf is empty if missing" {
        schema30().allOf.shouldBeEmpty()
        schema31().allOf.shouldBeEmpty()
        schema32().allOf.shouldBeEmpty()
    }

    "gets schema anyOf" {
        schema30("anyOf: [{}]").anyOf.size shouldBe 1
        schema31("anyOf: [{}]").anyOf.size shouldBe 1
        schema32("anyOf: [{}]").anyOf.size shouldBe 1
    }

    "gets schema anyOf is empty if missing" {
        schema30().anyOf.shouldBeEmpty()
        schema31().anyOf.shouldBeEmpty()
        schema32().anyOf.shouldBeEmpty()
    }

    "gets schema oneOf" {
        schema30("oneOf: [{}]").oneOf.size shouldBe 1
        schema31("oneOf: [{}]").oneOf.size shouldBe 1
        schema32("oneOf: [{}]").oneOf.size shouldBe 1
    }

    "gets schema oneOf is empty if missing" {
        schema30().oneOf.shouldBeEmpty()
        schema31().oneOf.shouldBeEmpty()
        schema32().oneOf.shouldBeEmpty()
    }

    "gets schema not" {
        schema30("not: {}").not.shouldNotBeNull()
        schema31("not: {}").not.shouldNotBeNull()
        schema32("not: {}").not.shouldNotBeNull()
    }

    "gets schema not is null if missing" {
        schema30().not.shouldBeNull()
        schema31().not.shouldBeNull()
        schema32().not.shouldBeNull()
    }

    "gets schema readOnly" {
        schema30("readOnly: true").readOnly.shouldBeTrue()
        schema31("readOnly: true").readOnly.shouldBeTrue()
        schema32("readOnly: true").readOnly.shouldBeTrue()
    }

    "gets schema readOnly is false if missing" {
        schema30().readOnly.shouldBeFalse()
        schema31().readOnly.shouldBeFalse()
        schema32().readOnly.shouldBeFalse()
    }

    "gets schema writeOnly" {
        schema30("writeOnly: true").writeOnly.shouldBeTrue()
        schema31("writeOnly: true").writeOnly.shouldBeTrue()
        schema32("writeOnly: true").writeOnly.shouldBeTrue()
    }

    "gets schema writeOnly is false if missing" {
        schema30().writeOnly.shouldBeFalse()
        schema31().writeOnly.shouldBeFalse()
        schema32().writeOnly.shouldBeFalse()
    }

    "gets schema externalDocs" {
        schema30("externalDocs: {}").externalDocs.shouldNotBeNull()
        schema31("externalDocs: {}").externalDocs.shouldNotBeNull()
        schema32("externalDocs: {}").externalDocs.shouldNotBeNull()
    }

    "gets schema externalDocs is null if missing" {
        schema30().externalDocs.shouldBeNull()
        schema31().externalDocs.shouldBeNull()
        schema32().externalDocs.shouldBeNull()
    }

    @Suppress("DEPRECATION")
    "gets schema example" {
        schema30("example: {}").example.shouldNotBeNull()
        schema31("example: {}").example.shouldNotBeNull()
        schema32("example: {}").example.shouldNotBeNull()
    }

    @Suppress("DEPRECATION")
    "gets schema example is null if missing" {
        schema30().example.shouldBeNull()
        schema31().example.shouldBeNull()
        schema32().example.shouldBeNull()
    }

    "gets schema discriminator" {
        schema30("discriminator: {}").discriminator.shouldNotBeNull()
        schema31("discriminator: {}").discriminator.shouldNotBeNull()
        schema32("discriminator: {}").discriminator.shouldNotBeNull()
    }

    "gets schema discriminator is null if missing" {
        schema30().discriminator.shouldBeNull()
        schema31().discriminator.shouldBeNull()
        schema32().discriminator.shouldBeNull()
    }

    "gets schema xml" {
        schema30("xml: {}").xml.shouldNotBeNull()
        schema31("xml: {}").xml.shouldNotBeNull()
        schema32("xml: {}").xml.shouldNotBeNull()
    }

    "gets schema xml is null if missing" {
        schema30().xml.shouldBeNull()
        schema31().xml.shouldBeNull()
        schema32().xml.shouldBeNull()
    }

    include(testExtensions("schema 30", ::schema30) { it.extensions })
    include(testExtensions("schema 31", ::schema31) { it.extensions })
    include(testExtensions("schema 32", ::schema32) { it.extensions })
})

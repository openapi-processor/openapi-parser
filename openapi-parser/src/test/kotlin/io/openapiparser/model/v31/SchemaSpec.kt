/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v31

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.maps.shouldContainKey
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.openapiparser.model.v31.schema as schema31
import io.openapiparser.model.v31.Schema as Schema31
import io.openapiparser.model.v32.schema as schema32
import io.openapiparser.model.v32.Schema as Schema32

/**
 * @see [io.openapiparser.model.v3x.SchemaSpec]
 */
class SchemaSpec: StringSpec({

    // todo
    // $schema
    // $vocabulary
    // $id
    // $dynamicRef
    // $comment
    // contentEncoding
    // contentMediaType
    // contentSchema
    // examples

    // $defs
    // if
    // then
    // else

    // xml

    "gets schema type" {
        schema31("type: string").type shouldBe listOf("string")
        schema31("type: [string, object]").type shouldBe listOf("string", "object")

        schema32("type: string").type shouldBe listOf("string")
        schema32("type: [string, object]").type shouldBe listOf("string", "object")
    }

//    "gets schema type throws if missing" {
//        shouldThrow<NoValueException> { schema().type }
//    }

    "gets schema const" {
        schema31("const: foo").const shouldBe "foo"
        schema32("const: foo").const shouldBe "foo"
    }

    "gets schema const is null if missing" {
        schema31().const.shouldBeNull()
        schema32().const.shouldBeNull()
    }

    "gets schema maxContains" {
        schema31("maxContains: 9").maxContains shouldBe 9
        schema32("maxContains: 9").maxContains shouldBe 9
    }

    "gets schema maxContains is null if missing" {
        schema31().maxContains.shouldBeNull()
        schema32().maxContains.shouldBeNull()
    }

    "gets schema minContains" {
        schema31("minContains: 9").minContains shouldBe 9
        schema32("minContains: 9").minContains shouldBe 9
    }

    "gets schema minContains is 1 if missing" {
        schema31().minContains shouldBe 1
        schema32().minContains shouldBe 1
    }

    "gets schema exclusiveMaximum is null if missing" {
        schema31().exclusiveMaximum.shouldBeNull()
        schema32().exclusiveMaximum.shouldBeNull()
    }

    "gets schema exclusiveMinimum is null if missing" {
        schema31().exclusiveMinimum.shouldBeNull()
        schema32().exclusiveMinimum.shouldBeNull()
    }

    "gets schema dependentRequired" {
        val required = schema31("dependentRequired: {bar: [foo]}").dependentRequired
        required shouldContainKey "bar"
        required["bar"].shouldContainExactly("foo")

        val required32 = schema32("dependentRequired: {bar: [foo]}").dependentRequired
        required32 shouldContainKey "bar"
        required32["bar"].shouldContainExactly("foo")
    }

    "gets schema dependentRequired is empty if missing" {
        schema31().dependentRequired.shouldNotBeNull()
        schema32().dependentRequired.shouldNotBeNull()
    }

    "gets schema patternProperties" {
        val source = """
          patternProperties:
            foo: {}
            bar: {}
        """

        val properties = schema31(source).patternProperties
        properties.size shouldBe 2
        properties["foo"].shouldNotBeNull()
        properties["bar"].shouldNotBeNull()

        val properties32 = schema32(source).patternProperties
        properties32.size shouldBe 2
        properties32["foo"].shouldNotBeNull()
        properties32["bar"].shouldNotBeNull()
    }

    "gets schema patternProperties is empty if missing" {
        schema31().patternProperties.shouldBeEmpty()
        schema32().patternProperties.shouldBeEmpty()
    }

    "gets schema additionalProperties" {
        schema31("additionalProperties: {}").additionalProperties.shouldBeInstanceOf<Schema31>()
        schema32("additionalProperties: {}").additionalProperties.shouldBeInstanceOf<Schema32>()
    }

    "gets schema additionalProperties boolean" {
        schema31("additionalProperties: true").additionalProperties shouldBe true
        schema31("additionalProperties: false").additionalProperties shouldBe false

        schema32("additionalProperties: true").additionalProperties shouldBe true
        schema32("additionalProperties: false").additionalProperties shouldBe false
    }

    "gets schema additionalProperties is null if missing" {
        schema31().additionalProperties.shouldBeNull()
        schema32().additionalProperties.shouldBeNull()
    }

    "gets schema propertyNames" {
        schema31("propertyNames: {}").propertyNames.shouldNotBeNull()
        schema32("propertyNames: {}").propertyNames.shouldNotBeNull()
    }

    "gets schema propertyNames is null if missing" {
        schema31().propertyNames.shouldBeNull()
        schema32().propertyNames.shouldBeNull()
    }

    "gets schema prefixItems" {
        schema31("prefixItems: [{}]").prefixItems.size shouldBe 1
        schema32("prefixItems: [{}]").prefixItems.size shouldBe 1
    }

    "gets schema prefixItems is empty if missing" {
        schema31().prefixItems.shouldBeEmpty()
        schema32().prefixItems.shouldBeEmpty()
    }

    "gets schema contains" {
        schema31("contains: {}").contains.shouldNotBeNull()
        schema32("contains: {}").contains.shouldNotBeNull()
    }

    "gets schema contains is null if missing" {
        schema31().contains.shouldBeNull()
        schema32().contains.shouldBeNull()
    }
})

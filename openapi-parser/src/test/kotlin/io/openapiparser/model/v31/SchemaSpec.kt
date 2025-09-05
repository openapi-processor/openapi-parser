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
import io.openapiparser.model.v31.schema as schema31
import io.openapiparser.model.v31.schema as schema32

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
    }

//    "gets schema type throws if missing" {
//        shouldThrow<NoValueException> { schema().type }
//    }

    "gets schema const" {
        schema31("const: foo").const shouldBe "foo"
    }

    "gets schema const is null if missing" {
        schema31().const.shouldBeNull()
    }

    "gets schema maxContains" {
        schema31("maxContains: 9").maxContains shouldBe 9
    }

    "gets schema maxContains is null if missing" {
        schema31().maxContains.shouldBeNull()
    }

    "gets schema minContains" {
        schema31("minContains: 9").minContains shouldBe 9
    }

    "gets schema minContains is 1 if missing" {
        schema31().minContains shouldBe 1
    }

    "gets schema exclusiveMaximum is null if missing" {
        schema31().exclusiveMaximum.shouldBeNull()
    }

    "gets schema exclusiveMinimum is null if missing" {
        schema31().exclusiveMinimum.shouldBeNull()
    }

    "gets schema dependentRequired" {
        val required = schema31("dependentRequired: {bar: [foo]}").dependentRequired
        required shouldContainKey "bar"
        required["bar"].shouldContainExactly("foo")
    }

    "gets schema dependentRequired is empty if missing" {
        schema31().dependentRequired.shouldNotBeNull()
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
    }

    "gets schema patternProperties is empty if missing" {
        schema31().patternProperties.shouldBeEmpty()
    }

    "gets schema additionalProperties" {
        schema31("additionalProperties: {}").additionalProperties.shouldNotBeNull()
    }

    "gets schema additionalProperties is null if missing" {
        schema31().additionalProperties.shouldBeNull()
    }

    "gets schema propertyNames" {
        schema31("propertyNames: {}").propertyNames.shouldNotBeNull()
    }

    "gets schema propertyNames is null if missing" {
        schema31().propertyNames.shouldBeNull()
    }

    "gets schema prefixItems" {
        schema31("prefixItems: [{}]").prefixItems.size shouldBe 1
    }

    "gets schema prefixItems is empty if missing" {
        schema31().prefixItems.shouldBeEmpty()
    }

    "gets schema contains" {
        schema31("contains: {}").contains.shouldNotBeNull()
    }

    "gets schema contains is null if missing" {
        schema31().contains.shouldBeNull()
    }
})

/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v31

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.maps.shouldContainKey
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.openapiparser.NoValueException

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

    // descriminator
    // xml
    // externalDocs
    // example

    "gets schema type" {
        schema("type: string").type shouldBe listOf("string")
        schema("type: [string, object]").type shouldBe listOf("string", "object")
    }

    "gets schema type throws if missing" {
        shouldThrow<NoValueException> { schema().type }
    }

    "gets schema const" {
        schema("const: foo").const shouldBe "foo"
    }

    "gets schema const is null if missing" {
        schema().const.shouldBeNull()
    }

    "gets schema maxContains" {
        schema("maxContains: 9").maxContains shouldBe 9
    }

    "gets schema maxContains is null if missing" {
        schema().maxContains.shouldBeNull()
    }

    "gets schema minContains" {
        schema("minContains: 9").minContains shouldBe 9
    }

    "gets schema minContains is 1 if missing" {
        schema().minContains shouldBe 1
    }

    "gets schema dependentRequired" {
        val required = schema("dependentRequired: {bar: [foo]}").dependentRequired
        required shouldContainKey "bar"
        required["bar"].shouldContainExactly("foo")
    }

    "gets schema dependentRequired is empty if missing" {
        schema().dependentRequired.shouldNotBeNull()
    }

    "gets schema patternProperties" {
        val source = """
          patternProperties:
            foo: {}
            bar: {}
        """

        val properties = schema(source).patternProperties
        properties.size shouldBe 2
        properties["foo"].shouldNotBeNull()
        properties["bar"].shouldNotBeNull()
    }

    "gets schema patternProperties is empty if missing" {
        schema().patternProperties.shouldBeEmpty()
    }

    "gets schema additionalProperties" {
        schema("additionalProperties: {}").additionalProperties.shouldNotBeNull()
    }

    "gets schema additionalProperties is null if missing" {
        schema().additionalProperties.shouldBeNull()
    }

    "gets schema propertyNames" {
        schema("propertyNames: {}").propertyNames.shouldNotBeNull()
    }

    "gets schema propertyNames is null if missing" {
        schema().propertyNames.shouldBeNull()
    }

    "gets schema prefixItems" {
        schema("prefixItems: [{}]").prefixItems.size shouldBe 1
    }

    "gets schema prefixItems is empty if missing" {
        schema().prefixItems.shouldBeEmpty()
    }

    "gets schema contains" {
        schema("contains: {}").contains.shouldNotBeNull()
    }

    "gets schema contains is null if missing" {
        schema().contains.shouldBeNull()
    }
})

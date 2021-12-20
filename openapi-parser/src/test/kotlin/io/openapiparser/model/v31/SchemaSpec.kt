/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v31

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.maps.shouldContainKey
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.openapiparser.NoValueException

/**
 * @see [io.openapiparser.model.v3x.SchemaSpec]
 */
class SchemaSpec: StringSpec({

    "gets schema type" {
        schema("type: string").type shouldBe listOf("string")
        schema("type: [string, object]").type shouldBe listOf("string", "object")
    }

    "gets schema type throws if missing" {
        shouldThrow<NoValueException> { schema().type }
    }

    // todo
    // enum
    // const

    //    additionalItems and items

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

})

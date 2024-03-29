/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import io.openapiprocessor.jsonschema.schema.Bucket
import io.openapiprocessor.jsonschema.schema.Bucket.createBucket
import io.openapiprocessor.jsonschema.schema.Scope.empty

class PropertiesNumberSpec: StringSpec({

    // number

    "get number is null if missing" {
        Properties(mockk(), Bucket.empty()).getNumberOrNull("missing").shouldBeNull()
    }

    "get number" {
        val bucket = createBucket(empty(), linkedMapOf<String, Any>("property" to 9.9))
        Properties(mockk(), bucket).getNumberOrNull("property").shouldBe(9.9)
    }

    // integer

    "get integer is null if missing" {
        Properties(mockk(), Bucket.empty()).getIntegerOrNull("missing").shouldBeNull()
    }

    "get integer" {
        val bucket = createBucket(empty(), linkedMapOf<String, Any>("property" to 9))
        Properties(mockk(), bucket).getIntegerOrNull("property").shouldBe(9)
    }

    "gets integer is default value if missing" {
        val props = Properties(mockk(), Bucket.empty())
        props.getIntegerOrDefault("missing", 9).shouldBe(9)
    }
})

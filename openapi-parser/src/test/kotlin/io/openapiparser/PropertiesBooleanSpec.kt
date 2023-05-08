/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldBeNull
import io.mockk.mockk
import io.openapiprocessor.jsonschema.schema.Bucket

class PropertiesBooleanSpec: StringSpec({

    "get boolean is null if missing" {
        Properties(mockk(), Bucket.empty()).getBooleanOrNull("missing").shouldBeNull()
    }

    "get boolean" {
        val bucket =
            Bucket(linkedMapOf<String, Any>("property" to true))
        Properties(mockk(), bucket).getBooleanOrNull("property")?.shouldBeTrue()
    }

    "gets boolean is default if value is missing" {
        val props = Properties(mockk(), Bucket.empty())
        props.getBooleanOrDefault("missing", true).shouldBeTrue()
        props.getBooleanOrDefault("missing", false).shouldBeFalse()
    }

    "get boolean or false if missing" {
        val props = Properties(mockk(), Bucket.empty())
        props.getBooleanOrFalse("missing").shouldBeFalse()
    }
})

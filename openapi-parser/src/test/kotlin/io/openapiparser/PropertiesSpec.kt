/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.mockk
import io.openapiparser.converter.NoValueException
import io.openapiparser.model.v30.OpenApi
import io.openapiparser.schema.Bucket

class PropertiesSpec: StringSpec({

    // raw value

    "get raw value is null if missing" {
        Properties(mockk(), Bucket.empty()).getRawValue("missing").shouldBeNull()
    }

    "get raw value" {
        val bucket = Bucket(linkedMapOf<String, Any>("foo" to "bar"))
        Properties(mockk(), bucket).getRawValue("foo").shouldBe("bar")
    }

    // object

    "gets object is null if missing" {
        val props = Properties(mockk(), Bucket.empty())
        props.getObjectOrNull("missing", OpenApi::class.java).shouldBeNull()
    }

    "gets object" {
        val bucket = Bucket(linkedMapOf<String, Any>("foo" to mapOf<String, Any>()))
        val props = Properties(mockk(), bucket)

        props.getObjectOrNull("missing", OpenApi::class.java).shouldBeInstanceOf<OpenApi>()
    }

    "get object throws if it is missing" {
        val props = Properties(mockk(), Bucket.empty())

        shouldThrow<NoValueException> {
                props.getObjectOrThrow("missing", OpenApi::class.java)
        }
    }
})

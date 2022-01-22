/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import io.openapiparser.converter.NoValueException
import io.openapiparser.converter.TypeMismatchException
import io.openapiparser.schema.Bucket

class PropertiesStringSpec: StringSpec({

    // plain string

    "gets nullable string" {
        Properties(mockk(), Bucket.empty()).getStringOrNull("missing").shouldBeNull()
    }

    "gets string" {
        val bucket = Bucket(linkedMapOf<String, Any>("property" to "foo"))
        Properties(mockk(), bucket).getStringOrThrow("property") shouldBe "foo"
    }

    "get string throws if value is not a string" {
        val bucket = Bucket(linkedMapOf<String, Any>("property" to 1))
        shouldThrow<TypeMismatchException> {
            Properties(mockk(), bucket).getStringOrThrow("property")
        }
    }

    "get string throws if it is missing" {
        shouldThrow<NoValueException> {
            Properties(mockk(), Bucket.empty()).getStringOrThrow("missing")
        }
    }

    // string array

    "gets nullable string array is null if missing" {
        Properties(mockk(), Bucket.empty()).getStringsOrNull("missing").shouldBeNull()
    }

    "gets nullable string array" {
        val bucket = Bucket(linkedMapOf<String, Any>("property" to listOf("foo", "bar")))
        Properties(mockk(), bucket).getStringsOrNull("property")
            .shouldContainExactly("foo", "bar")
    }

    "gets string array is empty if missing" {
        Properties(mockk(), Bucket.empty()).getStringsOrEmpty("missing").isEmpty()
    }

    "gets string array" {
        val bucket = Bucket(linkedMapOf<String, Any>("property" to listOf("foo", "bar")))
        Properties(mockk(), bucket).getStringsOrEmpty("property")
            .shouldContainExactly("foo", "bar")
    }

    // todo
    "gets string array throws if values are not strings".config(enabled = false) {
        val bucket = Bucket(linkedMapOf<String, Any>("property" to listOf(1, 2, 3)))
        shouldThrow<TypeMismatchException> {
            Properties(mockk(), bucket).getStringsOrNull("property")
        }
    }

})

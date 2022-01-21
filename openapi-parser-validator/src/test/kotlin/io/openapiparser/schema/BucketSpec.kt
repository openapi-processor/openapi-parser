/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.maps.shouldContainExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.openapiparser.converter.TypeMismatchException
import java.net.URI

class BucketSpec: StringSpec({

    "get document uri" {
        Bucket.empty().source.shouldBeNull()
        Bucket(URI("https://foo"), mapOf()).source.shouldBe(URI("https://foo"))
    }

    "get raw property value" {
        val bucket = Bucket(URI(""), mapOf(
            "1" to 1,
            "foo" to "foo",
            "bar" to true
        ))

        bucket.getRawValue("1") shouldBe 1
        bucket.getRawValue("foo") shouldBe "foo"
        bucket.getRawValue("bar") shouldBe true
    }

    "get raw properties" {
        val properties = mapOf(
            "1" to 1,
            "foo" to "foo",
            "bar" to true
        )

        val bucket = Bucket(URI(""), properties)

        bucket.rawValues shouldContainExactly properties
    }

    "has property" {
        val properties = mapOf("foo" to "bar")
        val bucket = Bucket(URI(""), properties)

        bucket.hasProperty("foo").shouldBeTrue()
        bucket.hasProperty("bar").shouldBeFalse()
    }

    "convert bucket property to object" {
        val bucket = Bucket(URI(""), "/location", mapOf(
            "foo" to mapOf<String, String>()
        ))

        bucket.convert<String>("foo") { name, value, location ->
            name.shouldBe("foo")
            value.shouldBeInstanceOf<Map<String, String>>()
            value.isEmpty()
            location.shouldBe("/location/foo")
            return@convert "called"
        } shouldBe "called"
    }

    "convert bucket to object" {
        val bucket = Bucket(URI(""), "/me", mapOf(
            "foo" to mapOf<String, String>()
        ))

        bucket.convert<String> { value, location ->
            value.shouldBeInstanceOf<Map<String, String>>()
            value.shouldContainExactly(mapOf("foo" to mapOf<String, String>()))
            location.shouldBe("/me")
            return@convert "called"
        } shouldBe "called"
    }

    "get property child bucket" {
        val bucket = Bucket(URI("https//document"), "/me", mapOf(
            "foo" to mapOf("key" to "value")
        ))

        val foo = bucket.getBucket("foo")
        foo?.rawValues?.shouldContainExactly(mapOf("key" to "value"))
        foo?.source shouldBe URI("https//document")
        foo?.location.toString() shouldBe "/me/foo"
    }

    "get property child bucket is null if property is missing" {
        val bucket = Bucket(URI("https//document"), "/me", mapOf())

        bucket.getBucket("bar").shouldBeNull()
    }

    "get property child bucket thrwos if property is not an object" {
        val bucket = Bucket(URI("https//document"), "/me", mapOf(
            "foo" to "bar"
        ))

        shouldThrow<TypeMismatchException> {
            bucket.getBucket("foo")
        }
    }
})

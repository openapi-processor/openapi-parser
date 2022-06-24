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
import io.openapiparser.converter.NoValueException
import io.openapiparser.converter.TypeMismatchException
import java.net.URI

class BucketSpec: StringSpec({

    "get document uri" {
        Bucket.empty().source.shouldBe(URI(""))
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
        val bucket = Bucket(URI("https://document"), "/me", mapOf(
            "foo" to mapOf("key" to "value")
        ))

        val foo = bucket.getBucket("foo")
        foo?.rawValues?.shouldContainExactly(mapOf("key" to "value"))
        foo?.source shouldBe URI("https://document")
        foo?.location.toString() shouldBe "/me/foo"
    }

    "get property child bucket is null if property is missing" {
        val bucket = Bucket(URI("https://document"), "/me", mapOf())

        bucket.getBucket("bar").shouldBeNull()
    }

    "get property child bucket throws if property is not an object" {
        val bucket = Bucket(URI("https://document"), "/me", mapOf(
            "foo" to "bar"
        ))

        shouldThrow<TypeMismatchException> {
            bucket.getBucket("foo")
        }
    }

    "get object value by pointer" {
        val bucket = Bucket(URI("https://document"), "/me", mapOf(
            "foo" to "bar"
        ))
        val pointer = JsonPointer.from("/foo")

        bucket.getRawValue(pointer) shouldBe "bar"
    }

    "get object value by null pointer" {
        val bucket = Bucket(URI("https://document"), "/me", mapOf("foo" to "bar"))
        val pointer = JsonPointer.from(null)

        bucket.getRawValue(pointer) shouldBe bucket.rawValues
    }

    "throws if get object value is null" {
        val bucket = Bucket(URI("https://document"), "/me", mapOf())
        val pointer = JsonPointer.from("/object")

        shouldThrow<NoValueException> {
            bucket.getRawValue(pointer)
        }
    }

    "get array value by pointer" {
        val bucket = Bucket(URI("https://document"), "/me", mapOf(
            "foo" to listOf("bar"))
        )
        val pointer = JsonPointer.from("/foo/0")

        bucket.getRawValue(pointer) shouldBe "bar"
    }

    "throws if get array value by pointer index is null" {
        val bucket = Bucket(URI("https://document"), "/me", mapOf(
            "array" to listOf(null)
        ))

        val pointer = JsonPointer.from("/array/0")

        shouldThrow<NoValueException> {
            bucket.getRawValue(pointer)
        }
    }

    "throws if get array value by pointer index does not exist" {
        val bucket = Bucket(URI("https://document"), "/me", mapOf(
            "array" to listOf<Any>()
        ))

        val pointer = JsonPointer.from("/array/0")

        shouldThrow<ArrayIndexOutOfBoundsException> {
            bucket.getRawValue(pointer)
        }
    }

    "get raw value without scope with scope of parent" {
        val bucket = Bucket(URI("https://host/document"), "/me", mapOf(
            "definitions" to mapOf<String, Any>(
                "foo" to mapOf<String, Any>(
                    "id" to "fooId",
                    "definitions" to mapOf<String, Any>(
                        "bar" to mapOf<String, Any>(
                            "type" to "string"
                        )
                    )
                )
            )
        ))

        val pointer = JsonPointer.from("/definitions/foo/definitions/bar")
        val rawValue = bucket.getRawValue(pointer, SchemaVersion.Draft4.idProvider)

        rawValue?.scope.toString() shouldBe "https://host/fooId"
    }

    "get raw value with scope" {
        val bucket = Bucket(URI("https://host/document"), "/me", mapOf(
            "definitions" to mapOf<String, Any>(
                "foo" to mapOf<String, Any>(
                    "id" to "fooId",
                    "definitions" to mapOf<String, Any>(
                        "bar" to mapOf<String, Any>(
                            "id" to "barId",
                            "type" to "string"
                        )
                    )
                )
            )
        ))

        val pointer = JsonPointer.from("/definitions/foo/definitions/bar")
        val rawValue = bucket.getRawValue(pointer, SchemaVersion.Draft4.idProvider)

        rawValue?.scope.toString() shouldBe "https://host/barId"
    }

    "get raw value with scope does not duplicate self scope" {
        val bucket = Bucket(URI("https://host/document/self"), "/", mapOf(
            "id" to "self"
        ))

        val pointer = JsonPointer.EMPTY
        val rawValue = bucket.getRawValue(pointer, SchemaVersion.Draft4.idProvider)

        rawValue?.scope.toString() shouldBe "https://host/document/self"
    }

    "get raw value with scope does not duplicate self scope with /" {
        val bucket = Bucket(URI("https://host/document/self/"), "/", mapOf(
            "id" to "self/"
        ))

        val pointer = JsonPointer.empty()
        val rawValue = bucket.getRawValue(pointer, SchemaVersion.Draft4.idProvider)

        rawValue?.scope.toString() shouldBe "https://host/document/self/"
    }
})

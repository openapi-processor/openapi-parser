/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.schema

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.maps.shouldContainExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.openapiprocessor.jsonschema.converter.NoValueException
import io.openapiprocessor.jsonschema.converter.TypeMismatchException
import io.openapiprocessor.jsonschema.schema.Scope.createScope
import io.openapiprocessor.jsonschema.support.Uris.*
import java.net.URI

class BucketSpec: StringSpec({
    val uri = URI("https://foo")

    "get document uri" {
        Bucket.empty().baseUri.shouldBe(emptyUri())

        Bucket(createScope(uri), mapOf()).baseUri.shouldBe(uri)
    }

    "get raw property value" {
        val bucket = Bucket(createScope(emptyUri()), mapOf(
                "1" to 1,
                "foo" to "foo",
                "bar" to true
            )
        )

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

        val bucket = Bucket(createScope(emptyUri()), properties)

        bucket.rawValues shouldContainExactly properties
    }

    "has property" {
        val properties = mapOf("foo" to "bar")
        val bucket = Bucket(createScope(emptyUri()), properties)

        bucket.hasProperty("foo").shouldBeTrue()
        bucket.hasProperty("bar").shouldBeFalse()
    }

    "convert bucket property to object" {
        val bucket = Bucket(
            Scope.empty(),
            JsonPointer.from("/location"),
            mapOf("foo" to mapOf<String, String>())
        )

        bucket.convert<String>("foo") { name, value, location ->
            name.shouldBe("foo")
            value.shouldBeInstanceOf<Map<String, String>>()
            value.isEmpty()
            location.shouldBe("/location/foo")
            return@convert "called"
        } shouldBe "called"
    }

    "convert bucket to object" {
        val bucket = Bucket(
            Scope.empty(),
            JsonPointer.from("/me"),
            mapOf("foo" to mapOf<String, String>())
        )

        bucket.convert<String> { value, location ->
            value.shouldBeInstanceOf<Map<String, String>>()
            value.shouldContainExactly(mapOf("foo" to mapOf<String, String>()))
            location.shouldBe("/me")
            return@convert "called"
        } shouldBe "called"
    }

    "get property child bucket" {
        val bucket = Bucket(
            createScope(createUri("https://document")),
            JsonPointer.from("/me"),
            mapOf("foo" to mapOf("key" to "value"))
        )

        val foo = bucket.getBucket("foo")
        foo?.rawValues?.shouldContainExactly(mapOf("key" to "value"))
        foo?.baseUri shouldBe URI("https://document")
        foo?.location.toString() shouldBe "/me/foo"
    }

    "get property child bucket is null if property is missing" {
        val bucket = Bucket(
            createScope(createUri("https://document")),
            JsonPointer.from("/me"),
            mapOf()
        )

        bucket.getBucket("bar").shouldBeNull()
    }

    "get property child bucket throws if property is not an object" {
        val bucket = Bucket(
            createScope(createUri("https://document")),
            JsonPointer.from("/me"),
            mapOf("foo" to "bar")
        )

        shouldThrow<TypeMismatchException> {
            bucket.getBucket("foo")
        }
    }

    "get object value by pointer" {
        val bucket = Bucket(
            createScope(createUri("https://document")),
            JsonPointer.from("/me"),
            mapOf("foo" to "bar")
        )

        bucket.getRawValueValue(JsonPointer.from("/foo")) shouldBe "bar"
    }

    "get object value by null pointer" {
        val bucket = Bucket(
            createScope(createUri("https://document")),
            JsonPointer.from("/me"),
            mapOf("foo" to "bar")
        )

        bucket.getRawValueValue(JsonPointer.from(null)) shouldBe bucket.rawValues
    }

    "throws if get object value is null" {
        val bucket = Bucket(
            createScope(createUri("https://document")),
            JsonPointer.from("/me"),
            mapOf())

        shouldThrow<NoValueException> {
            bucket.getRawValueValue(JsonPointer.from("/object"))
        }
    }

    "get array value by pointer" {
        val bucket = Bucket(
            createScope(createUri("https://document")),
            JsonPointer.from("/me"),
            mapOf("foo" to listOf("bar"))
        )

        bucket.getRawValueValue(JsonPointer.from("/foo/0")) shouldBe "bar"
    }

    "throws if get array value by pointer index is null" {
        val bucket = Bucket(
            createScope(createUri("https://document")),
            JsonPointer.from("/me"),
            mapOf("array" to listOf(null))
        )

        shouldThrow<NoValueException> {
            bucket.getRawValueValue(JsonPointer.from("/array/0"))
        }
    }

    "throws if get array value by pointer index does not exist" {
        val bucket = Bucket(
            createScope(createUri("https://document")),
            JsonPointer.from("/me"),
            mapOf("array" to listOf<Any>())
        )

        shouldThrow<ArrayIndexOutOfBoundsException> {
            bucket.getRawValueValue(JsonPointer.from("/array/0"))
        }
    }

    "get raw value, without own scope is scope of parent" {
        val scope = Scope(createUri("https://host/document"), SchemaVersion.Draft4)
        val bucket = Bucket(
            scope, "/any", mapOf(
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
            )
        )

        val pointer = JsonPointer.from("/definitions/foo/definitions/bar")
        val rawValue = bucket.getRawValue(pointer)

        rawValue?.scope?.baseUri.toString() shouldBe "https://host/fooId"
    }

    "get raw value, with scope is value scope" {
        val scope = Scope(createUri("https://host/document"), SchemaVersion.Draft4)
        val bucket = Bucket(
            scope, "/any", mapOf(
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
            )
        )

        val pointer = JsonPointer.from("/definitions/foo/definitions/bar")
        val rawValue = bucket.getRawValue(pointer)

        rawValue?.scope?.baseUri.toString() shouldBe "https://host/barId"
    }

    "get raw value, does not duplicate parent scope" {
        val scope = Scope(createUri("https://host/document/self"), SchemaVersion.Draft4)
        val bucket = Bucket(
            scope, "/any", mapOf(
                "id" to "self"
            )
        )

        val pointer = JsonPointer.EMPTY
        val rawValue = bucket.getRawValue(pointer)

        rawValue?.scope?.baseUri.toString() shouldBe "https://host/document/self"
    }

    "get raw value, does not duplicate parent scope with /" {
        val scope = Scope(createUri("https://host/document/self/"), SchemaVersion.Draft4)
        val bucket = Bucket(
            scope, "/", mapOf(
                "id" to "self/"
            )
        )

        val pointer = JsonPointer.empty()
        val rawValue = bucket.getRawValue(pointer)

        rawValue?.scope?.baseUri.toString() shouldBe "https://host/document/self/"
    }
})

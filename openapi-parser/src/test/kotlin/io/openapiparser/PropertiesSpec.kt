/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.mockk
import io.openapiprocessor.jsonschema.converter.NoValueException
import io.openapiprocessor.jsonschema.converter.TypeMismatchException
import io.openapiprocessor.jsonschema.schema.Bucket
import io.openapiprocessor.jsonschema.schema.Bucket.createBucket
import io.openapiprocessor.jsonschema.schema.SchemaVersion
import io.openapiprocessor.jsonschema.schema.Scope
import io.openapiprocessor.jsonschema.schema.Scope.createScope
import io.openapiprocessor.jsonschema.schema.UriSupport.createUri

class PropertiesSpec: StringSpec({
    @Suppress("UNUSED_PARAMETER")
    class DummyObject(context: Context?, bucket: Bucket?)
    val anyVersion = SchemaVersion.Draft4

    // raw value

    "get raw value is null if missing" {
        Properties(mockk(), Bucket.empty()).getRawValue("missing").shouldBeNull()
    }

    "get raw value" {
        val bucket = createBucket(Scope.empty(), linkedMapOf<String, Any>("foo" to "bar"))
        Properties(mockk(), bucket).getRawValue("foo").shouldBe("bar")
    }

    // object

    "gets object is null if missing" {
        val props = Properties(mockk(), Bucket.empty())
        props.getObjectOrNull("missing", DummyObject::class.java).shouldBeNull()
    }

    "gets object" {
        val bucket = createBucket(Scope.empty(), linkedMapOf<String, Any>("foo" to mapOf<String, Any>()))

        val scope = createScope(createUri("https://foo"), bucket.rawValues, anyVersion)
        val props = Properties(Context(scope, mockk()), bucket)

        props.getObjectOrNull("foo", DummyObject::class.java).shouldBeInstanceOf<DummyObject>()
    }

    "gets object throws if value is not an object" {
        val bucket = createBucket(Scope.empty(), linkedMapOf<String, Any>("foo" to "no object"))

        val scope = createScope(createUri("https://foo"), bucket.rawValues, anyVersion)
        val props = Properties(Context(scope, mockk()), bucket)

        shouldThrow<TypeMismatchException> {
            props.getObjectOrNull("foo", DummyObject::class.java)
        }
    }

    "get object throws if it is missing" {
        val props = Properties(mockk(), Bucket.empty())

        shouldThrow<NoValueException> {
                props.getObjectOrThrow("missing", DummyObject::class.java)
        }
    }

    // array

    "get object values is empty if value is missing" {
        val props = Properties(mockk(), Bucket.empty())

        props.getObjectsOrEmpty("property", DummyObject::class.java).shouldBeEmpty()
    }

    "get objects array" {
        val bucket = createBucket(
            Scope.empty(),
            linkedMapOf<String, Any>(
                "property" to listOf(
                    mapOf<String, Any>("foo" to "bar"),
                    mapOf<String, Any>("foos" to "bars")
                )
            )
        )

        val scope = createScope(createUri("https://foo"), bucket.rawValues, anyVersion)
        val props = Properties(Context(scope, mockk()), bucket)

        props.getObjectsOrEmpty("property", DummyObject::class.java).size shouldBe 2
    }

    "get objects array throws if any value is not an object" {
        val bucket = createBucket(
            Scope.empty(),
            linkedMapOf<String, Any>(
                "property" to listOf(
                    mapOf<String, Any>("foo" to "bar"),
                    "not an object"
                )
            )
        )

        val scope = createScope(createUri("https://foo"), bucket.rawValues, anyVersion)
        val props = Properties(Context(scope, mockk()), bucket)

        shouldThrow<TypeMismatchException> {
            props.getObjectsOrEmpty("property", DummyObject::class.java)
        }
    }

    "get extension values" {
        val bucket = createBucket(
            Scope.empty(),
            linkedMapOf<String, Any>(
                "property" to "foo",
                "x-foo" to "foo extension",
                "x-bar" to linkedMapOf<String, Any>()
            )
        )
        val props = Properties(mockk(), bucket)

        val extensions = props.extensions
        extensions.shouldNotBeNull()
        extensions.size shouldBe 2
    }

    "gets empty extension values if there are no extensions" {
        val bucket = createBucket(
            Scope.empty(),
            linkedMapOf<String, Any>(
                "property" to "foo",
            )
        )
        val props = Properties(mockk(), bucket)

        val extensions = props.extensions
        extensions.shouldNotBeNull()
        extensions.size shouldBe 0
    }
})

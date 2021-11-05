/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain

class NodeSpec : StringSpec({

    "gets nullable raw value" {
        Node.empty().getRawValue("missing").shouldBeNull()
    }

    "gets raw value" {
        val node = Node("$", linkedMapOf<String, Any>("property" to 1))
        node.getRawValue("property").shouldNotBeNull()
    }

    "gets nullable string value" {
        Node.empty().getStringValue("missing").shouldBeNull()
    }

    "gets string value" {
        val node = Node("$", linkedMapOf<String, Any>("property" to "foo"))
        node.getStringValue("property").shouldBe("foo")
    }

    "get string value throws if value is not a string" {
        val node = Node("$", linkedMapOf<String, Any>("property" to 1))
        shouldThrow<TypeMismatchException> {
            node.getStringValue("property")
        }
    }

    "gets required string value" {
        val node = Node("$", linkedMapOf<String, Any>("property" to "foo"))
        node.getRequiredStringValue("property").shouldBe("foo")
    }

    "gets required string value throws if value is missing" {
        shouldThrow<NoValueException> {
            Node.empty().getRequiredStringValue("missing")
        }
    }

    "gets nullable string values" {
        Node.empty().getStringValues("missing").shouldBeNull()
    }

    "gets string values" {
        val node = Node("$", linkedMapOf<String, Any>("property" to listOf("foo", "bar")))
        node.getStringValues("property").shouldContainExactly("foo", "bar")
    }

    "gets string values throws if values are not strings" {
        val node = Node("$", linkedMapOf<String, Any>("property" to listOf(1, 2, 3)))
        shouldThrow<TypeMismatchException> {
            node.getStringValues("property")
        }
    }

    "gets object value" {
        val node = Node("$", linkedMapOf<String, Any>("property" to emptyMap<String, Any>()))
        node.getObjectValue("property").shouldNotBeNull()
    }

    "gets object value throws if value is missing" {
        shouldThrow<NoObjectException> {
            Node.empty().getObjectValue("property")
        }
    }

    "gets object value throws if value is not an object" {
        val node = Node("$", linkedMapOf<String, Any>("property" to "bad type"))
        shouldThrow<NoObjectException> {
            node.getObjectValue("property")
        }
    }

    "gets required object value throws if value is missing" {
        shouldThrow<NoValueException> {
            Node.empty().getRequiredObjectValue("missing") {}
        }
    }

    "gets object values throws if value is missing" {
            shouldThrow<NoArrayException> {
            Node.empty().getObjectValues("property")
        }
    }

    "gets object values throws if value is no collection" {
        shouldThrow<NoArrayException> {
            Node.empty().getObjectValues("property")
        }
    }

    "gets object values throws if any value is not an object" {
        val node = Node("$", linkedMapOf<String, Any>("property" to listOf(
            mapOf<String, Any>("foo" to "bar"),
            "not an object"
        )))

        shouldThrow<NoObjectException> {
            node.getObjectValues("property")
        }
    }

    "gets object values" {
        val node = Node("$", linkedMapOf<String, Any>("property" to listOf(
            mapOf<String, Any>("foo" to "bar"),
            mapOf<String, Any>("foos" to "bars")
        )))

        val objects = node.getObjectValues("property")
        objects.size shouldBe 2
    }

    "gets nullable object values" {
        Node.empty().getObjectValues("missing") {}.shouldBeNull()
    }

    "converts to null if property does not exists" {
        Node.empty().getObjectValue("unknown") {}.shouldBeNull()
    }

    "getting required string property (as String) reports full json path if it is missing" {
        val ex = shouldThrow<NoValueException> {
            Node("$", emptyMap()).getRequiredStringValue("property")
        }

        ex.message shouldContain "$.property"
    }

})


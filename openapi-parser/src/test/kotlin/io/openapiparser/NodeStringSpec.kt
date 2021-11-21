/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain

class NodeStringSpec : StringSpec({

    // plain string

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

    "getting required string property (as String) reports full json path if it is missing" {
        val ex = shouldThrow<NoValueException> {
            Node("$", emptyMap()).getRequiredStringValue("property")
        }

        ex.message shouldContain "$.property"
    }

    // string array

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

    "gets not empty string values" {
        val node = Node("$", linkedMapOf<String, Any>("property" to listOf("foo", "bar")))
        node.getStringValuesOrEmpty("property").shouldContainExactly("foo", "bar")
    }

    "gets empty array if property is missing" {
        Node.empty().getStringValuesOrEmpty("missing").shouldBeEmpty()
    }

})


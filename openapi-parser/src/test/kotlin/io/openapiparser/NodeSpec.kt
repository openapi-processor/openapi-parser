/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

class NodeSpec : StringSpec({

    // raw value

    "gets nullable raw value" {
        Node.empty().getRawValue("missing").shouldBeNull()
    }

    "gets raw value" {
        val node = Node("$", linkedMapOf<String, Any>("property" to 1))
        node.getRawValue("property").shouldNotBeNull()
    }

    // node value

    "gets object value" {
        val node = Node("$", linkedMapOf<String, Any>("property" to emptyMap<String, Any>()))
        node.getNode("property").shouldNotBeNull()
    }

    "gets object value throws if value is missing" {
        shouldThrow<TypeMismatchException> {
            Node.empty().getNode("property")
        }
    }

    "gets object value throws if value is not an object" {
        val node = Node("$", linkedMapOf<String, Any>("property" to "bad type"))
        shouldThrow<TypeMismatchException> {
            node.getNode("property")
        }
    }

    // node array

    "gets object values throws if value is missing" {
            shouldThrow<TypeMismatchException> {
            Node.empty().getNodes("property")
        }
    }

    "gets object values throws if value is no collection" {
        shouldThrow<TypeMismatchException> {
            Node.empty().getNodes("property")
        }
    }

    "gets object values throws if any value is not an object" {
        val node = Node("$", linkedMapOf<String, Any>("property" to listOf(
            mapOf<String, Any>("foo" to "bar"),
            "not an object"
        )))

        shouldThrow<TypeMismatchException> {
            node.getNodes("property")
        }
    }

    "gets object values" {
        val node = Node("$", linkedMapOf<String, Any>("property" to listOf(
            mapOf<String, Any>("foo" to "bar"),
            mapOf<String, Any>("foos" to "bars")
        )))

        val objects = node.getNodes("property")
        objects.size shouldBe 2
    }

    "gets extension values" {
        val node = Node("$", linkedMapOf<String, Any>(
            "property" to "foo",
            "x-foo" to "foo extension",
            "x-bar" to linkedMapOf<String, Any>()
        ))

        val extensions = node.extensions
        extensions.shouldNotBeNull()
        extensions.size shouldBe 2
    }

    "gets empty extension values if there are no extensions" {
        val node = Node("$", linkedMapOf<String, Any>(
            "property" to "foo"
        ))

        val extensions = node.extensions
        extensions.shouldNotBeNull()
        extensions.size shouldBe 0
    }
})


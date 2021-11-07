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
        node.getObjectNode("property").shouldNotBeNull()
    }

    "gets object value throws if value is missing" {
        shouldThrow<NoObjectException> {
            Node.empty().getObjectNode("property")
        }
    }

    "gets object value throws if value is not an object" {
        val node = Node("$", linkedMapOf<String, Any>("property" to "bad type"))
        shouldThrow<NoObjectException> {
            node.getObjectNode("property")
        }
    }

    // node array

    "gets object values throws if value is missing" {
            shouldThrow<NoArrayException> {
            Node.empty().getObjectNodes("property")
        }
    }

    "gets object values throws if value is no collection" {
        shouldThrow<NoArrayException> {
            Node.empty().getObjectNodes("property")
        }
    }

    "gets object values throws if any value is not an object" {
        val node = Node("$", linkedMapOf<String, Any>("property" to listOf(
            mapOf<String, Any>("foo" to "bar"),
            "not an object"
        )))

        shouldThrow<NoObjectException> {
            node.getObjectNodes("property")
        }
    }

    "gets object values" {
        val node = Node("$", linkedMapOf<String, Any>("property" to listOf(
            mapOf<String, Any>("foo" to "bar"),
            mapOf<String, Any>("foos" to "bars")
        )))

        val objects = node.getObjectNodes("property")
        objects.size shouldBe 2
    }

})


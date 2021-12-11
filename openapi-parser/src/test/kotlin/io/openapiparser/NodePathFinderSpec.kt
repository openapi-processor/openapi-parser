/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf

class NodePathFinderSpec : StringSpec({

    "returns null for unknown path" {
        NodePathFinder(Node("$", mapOf())).find("/unknown").shouldBeNull()
    }

    "find by single level path" {
        val found = NodePathFinder(Node("$", mapOf("key" to "value"))).find("/key")

        found.shouldBeInstanceOf<String>()
        found.shouldBe("value")
    }

    "find by multi level path" {
        val found = NodePathFinder(Node(
            "$",
            mapOf("level 0" to
                mapOf("level 1" to
                    mapOf("level 2" to
                        mapOf("target" to
                            mapOf<String, Any>("key" to "value")))))
        )).find("/level 0/level 1/level 2/target")

        found.shouldBeInstanceOf<Map<String, Any>>()
        found["key"].shouldBe("value")
    }

})

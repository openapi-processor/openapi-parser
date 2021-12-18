/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe

class NodeNumberSpec: StringSpec({

    // number

    "gets nullable number value" {
        Node.empty().getNumberValue("missing").shouldBeNull()
    }

    "gets number value" {
        val node = Node("$", linkedMapOf<String, Any>("property" to 9.9))
        node.getNumberValue("property").shouldBe(9.9)
    }

    "gets nullable number default value" {
        Node.empty().getNumberValue("missing", 9.9) shouldBe 9.9
    }

    // integer

    "gets nullable integer value" {
        Node.empty().getIntegerValue("missing").shouldBeNull()
    }

    "gets integer value" {
        val node = Node("$", linkedMapOf<String, Any>("property" to 9))
        node.getIntegerValue("property").shouldBe(9)
    }

    "gets nullable integer default value" {
        Node.empty().getIntegerValue("missing", 9) shouldBe 9
    }
})

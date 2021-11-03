/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.string.shouldContain

class NodeSpec : StringSpec({

    "converts to null if property does not exists" {
        Node.empty().getPropertyAs("unknown") {}.shouldBeNull()
    }

    "getting required string property (as String) reports full json path if it is missing" {
        val ex = shouldThrow<NullPropertyException> {
            Node("$", emptyMap()).getRequiredStringValue("property")
        }

        ex.message shouldContain "$.property"
    }

})


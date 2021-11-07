/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull

class NodeObjectSpec : StringSpec({

    "gets required object value throws if value is missing" {
        shouldThrow<NoValueException> {
            Node.empty().getRequiredObjectValue("missing") {}
        }
    }

    "converts to null if property does not exists" {
        Node.empty().getObjectValue("unknown") {}.shouldBeNull()
    }

    "gets nullable object values" {
        Node.empty().getObjectValues("missing") {}.shouldBeNull()
    }

})


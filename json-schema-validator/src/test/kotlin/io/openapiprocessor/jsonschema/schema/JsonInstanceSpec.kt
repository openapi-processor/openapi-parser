/*
 * Copyright 2025 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.schema

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class JsonInstanceSpec : StringSpec({

    "get null array value" {
        val instance = JsonInstance(listOf(null))
        val result = instance.getValue(0)
        result.rawValue shouldBe null
    }

})

/*
 * Copyright 2023 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.converter

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.openapiprocessor.jsonschema.converter.IntegerConverter
import io.openapiprocessor.jsonschema.schema.JsonPointer.empty

class IntegerConverterSpec : StringSpec({

    "x.0 is an integer" {
        val converter = IntegerConverter()

        val value = converter.convert("int", 1.0, empty())

        value.shouldBe(1)
    }

})

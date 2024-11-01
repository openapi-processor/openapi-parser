/*
 * Copyright 2024 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.ov10

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.openapiparser.model.v30.overlay
import io.openapiprocessor.jsonschema.converter.NoValueException

class OverlaySpec: StringSpec({

    "get overlay version" {
        overlay("overlay: 1.0.0").overlay shouldBe "1.0.0"
    }

    "get overlay version throws if it is missing" {
        shouldThrow<NoValueException> { overlay().overlay }
    }
})

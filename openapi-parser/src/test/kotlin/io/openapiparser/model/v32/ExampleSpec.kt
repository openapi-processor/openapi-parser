/*
 * Copyright 2025 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v32

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

class ExampleSpec: StringSpec({

    "gets example data value" {
        example("dataValue: {}").dataValue.shouldNotBeNull()
    }

    "gets example data value is null if missing" {
        example().dataValue.shouldBeNull()
    }

    "gets example serialized value" {
        example("serializedValue: url").serializedValue shouldBe "url"
    }

    "gets example serialized value is null if missing" {
        example().serializedValue.shouldBeNull()
    }
})

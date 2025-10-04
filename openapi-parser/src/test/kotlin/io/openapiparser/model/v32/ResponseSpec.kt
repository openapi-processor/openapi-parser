/*
 * Copyright 2025 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v32

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe

class ResponseSpec: StringSpec({

    "gets response summary" {
        response("summary: a summary").summary shouldBe "a summary"
    }

    "gets response summary is null if missing" {
        response().summary.shouldBeNull()
    }
})

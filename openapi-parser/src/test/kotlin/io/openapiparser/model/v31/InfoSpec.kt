/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v31

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe

class InfoSpec : StringSpec({

    "gets info summary" {
        info("summary: the summary").summary shouldBe "the summary"
    }

    "gets info summary is null if missing" {
        info().summary.shouldBeNull()
    }
})

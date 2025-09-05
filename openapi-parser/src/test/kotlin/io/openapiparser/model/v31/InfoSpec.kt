/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v31

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.openapiparser.model.v31.info as info31
import io.openapiparser.model.v32.info as info32

/**
 * @see [io.openapiparser.model.v3x.InfoSpec]
 */
class InfoSpec : StringSpec({

    "gets info summary" {
        info31("summary: the summary").summary shouldBe "the summary"
        info32("summary: the summary").summary shouldBe "the summary"
    }

    "gets info summary is null if missing" {
        info31().summary.shouldBeNull()
        info32().summary.shouldBeNull()
    }
})

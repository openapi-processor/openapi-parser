/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v3x

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.openapiparser.model.v30.example as example30
import io.openapiparser.model.v31.example as example31

class ExampleSpec: StringSpec({

    "gets example summary" {
        example30("summary: a summary").summary shouldBe "a summary"
        example31("summary: a summary").summary shouldBe "a summary"
    }

    "gets path item summary is null if missing" {
        example30().summary.shouldBeNull()
        example31().summary.shouldBeNull()
    }

    include(testDescription("example 30", ::example30) { it.description })
    include(testDescription("example 31", ::example31) { it.description })

    include(testExtensions("example 30", ::example30) { it.extensions })
    include(testExtensions("example 31", ::example31) { it.extensions })
})

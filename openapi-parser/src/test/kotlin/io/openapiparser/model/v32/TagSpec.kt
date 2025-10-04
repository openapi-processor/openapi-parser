/*
 * Copyright 2025 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v32

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.openapiparser.model.v32.tag as tag32

class TagSpec: StringSpec({

    "gets tag summary" {
        tag32("summary: a summary").summary shouldBe "a summary"
    }

    "gets tag summary is null if missing" {
        tag32().summary.shouldBeNull()
    }

    "gets tag parent" {
        tag32("parent: the parent").parent shouldBe "the parent"
    }

    "gets tag name is null if missing" {
        tag32().parent.shouldBeNull()
    }

    "gets tag kind" {
        tag32("kind: the kind").kind shouldBe "the kind"
    }

    "gets tag kind is null if missing" {
        tag32().kind.shouldBeNull()
    }
})

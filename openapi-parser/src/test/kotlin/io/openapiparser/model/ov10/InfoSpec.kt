/*
 * Copyright 2024 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.ov10

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.openapiparser.model.v3x.testExtensions
import io.openapiprocessor.jsonschema.converter.NoValueException

class InfoSpec: StringSpec({

    "gets info title" {
        info("title: the title").title shouldBe "the title"
    }

    "gets info title throws if it is missing" {
        shouldThrow<NoValueException> { info().title }
    }

    "gets info version" {
        info("""version: "1"""").version shouldBe "1"
    }

    "gets info version throws if it is missing" {
        shouldThrow<NoValueException> { info().version }
    }

    include(testExtensions("Info", ::info) { it.extensions })
})

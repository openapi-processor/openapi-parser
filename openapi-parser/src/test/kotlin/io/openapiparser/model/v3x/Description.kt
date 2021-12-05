/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v3x

import io.kotest.core.spec.style.stringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe

fun <O> testDescription(
    name: String,
    build: (content: String) -> O,
    extract: (o: O) -> String?
) = stringSpec {

    "gets $name description" {
        extract(build("description: a description")) shouldBe "a description"
    }

    "gets $name description is null if missing" {
        extract(build("{}")).shouldBeNull()
    }

}

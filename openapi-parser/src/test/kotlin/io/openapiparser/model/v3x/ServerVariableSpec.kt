/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v3x

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.openapiprocessor.jsonschema.converter.NoValueException
import io.openapiparser.model.v30.serverVariable as serverVariable30
import io.openapiparser.model.v31.serverVariable as serverVariable31

class ServerVariableSpec: StringSpec({

    "gets server variable enum" {
        serverVariable30("enum: [one, two]").enum shouldContainExactly (listOf("one", "two"))
        serverVariable31("enum: [one, two]").enum shouldContainExactly (listOf("one", "two"))
    }

    "gets empty server variable enum if it is missing" {
        serverVariable30().enum.shouldBeEmpty()
        serverVariable31().enum.shouldBeEmpty()
    }

    "gets server variable default" {
        serverVariable30("default: value").default shouldBe "value"
        serverVariable31("default: value").default shouldBe "value"
    }

    "gets server variable default throws if it is missing" {
        shouldThrow<NoValueException> { serverVariable30().default }
        shouldThrow<NoValueException> { serverVariable31().default }
    }

    include(testDescription("server variable 30", ::serverVariable30) { it.description })
    include(testDescription("server variable 31", ::serverVariable31) { it.description })

    include(testExtensions("server variable 30", ::serverVariable30) { it.extensions })
    include(testExtensions("server variable 31", ::serverVariable31) { it.extensions })
})


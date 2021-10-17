/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.openapiparser.support.TestBuilder

class OpenApiParserSpec: StringSpec({

    "detects openapi 3.0" {
        val parser = TestBuilder()
            .withApi("""
                openapi: 3.0.3
            """.trimIndent())
            .buildParser()

        val result = parser.parse()

        result.version shouldBe OpenApiResult.Version.V30
    }

    "validates openapi 3.0" {
        val parser = TestBuilder()
            .withApi("""
                openapi: 3.0.3
            """.trimIndent())
            .buildParser()

        val result = parser.parse()

        val messages = result.validationMessages
        messages.size shouldNotBe 0
    }

    "detects openapi 3.1" {
        val parser = TestBuilder()
            .withApi("""
                openapi: 3.1.0                
            """.trimIndent())
            .buildParser()

        val result = parser.parse()

        result.version shouldBe OpenApiResult.Version.V31
    }

    "validates openapi 3.1" {
        val parser = TestBuilder()
            .withApi("""
                openapi: 3.1.0
            """.trimIndent())
            .buildParser()

        val result = parser.parse()

        val messages = result.validationMessages
        messages.size shouldNotBe 0
    }

})

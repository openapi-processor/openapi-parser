/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.openapiparser.support.ApiBuilder
import java.net.URI

class OpenApiParserSpec: StringSpec({

    "detects openapi 3.0" {
        val parser = ApiBuilder()
            .withApi("""
                openapi: 3.0.3
            """.trimIndent())
            .buildParser()

        val result = parser.parse(URI(""))

        result.version shouldBe OpenApiResult.Version.V30
    }

    "detects openapi 3.1" {
        val parser = ApiBuilder()
            .withApi("""
                openapi: 3.1.0                
            """.trimIndent())
            .buildParser()

        val result = parser.parse(URI(""))

        result.version shouldBe OpenApiResult.Version.V31
    }
})

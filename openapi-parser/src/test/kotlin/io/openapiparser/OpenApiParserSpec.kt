package io.openapiparser

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class OpenApiParserSpec: StringSpec({

    "detects openapi 3.0" {
        val parser = TestParserBuilder()
            .withApi("""
                openapi: 3.0.3
            """.trimIndent())
            .build()

        val result = parser.parse()

        result.version shouldBe OpenApiResult.Version.V30
    }

    "detects openapi 3.1" {
        val parser = TestParserBuilder()
            .withApi("""
                openapi: 3.1.0                
            """.trimIndent())
            .build()

        val result = parser.parse()

        result.version shouldBe OpenApiResult.Version.V31
    }

})

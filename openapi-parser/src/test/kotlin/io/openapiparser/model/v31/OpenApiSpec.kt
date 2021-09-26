package io.openapiparser.model.v31

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.openapiparser.support.TestBuilder

class OpenApiSpec  : StringSpec({

    "gets openapi version" {
        val api = TestBuilder()
            .withApi(
                """
                openapi: 3.1.0
                info:
                  title: title
                  version: "1"
                paths: {}
            """.trimIndent()
            )
            .buildOpenApi31()

        api.openapi shouldBe "3.1.0"
    }

    "gets info object" {
        val api = TestBuilder()
            .withApi(
                """
                openapi: 3.1.0
                info:
                  title: the title
                  summary: summary
                  description: the description
                  termsOfService: https://any/terms 
                  version: "1"
            """.trimIndent()
            )
            .buildOpenApi31()

        val info = api.info
        info.title shouldBe "the title"
        info.summary shouldBe "summary"
        info.description shouldBe "the description"
        info.termsOfService shouldBe "https://any/terms"
        info.version shouldBe "1"
    }
})

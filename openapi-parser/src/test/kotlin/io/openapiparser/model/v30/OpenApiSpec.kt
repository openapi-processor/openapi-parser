package io.openapiparser.model.v30

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.openapiparser.support.TestBuilder

class OpenApiSpec : StringSpec({

    "gets openapi version" {
        val api = TestBuilder()
            .withApi("""
                openapi: 3.0.3
                info:
                  title: title
                  version: "1"
                paths: {}
            """.trimIndent())
            .buildOpenApi30()

        api.openapi shouldBe "3.0.3"
    }

    "gets info object" {
        val api = TestBuilder()
            .withApi("""
                openapi: 3.0.3
                info:
                  title: the title
                  description: the description
                  termsOfService: https://any/terms 
                  version: "1"
                paths: {}
            """.trimIndent())
            .buildOpenApi30()

        val info = api.info
        info.title shouldBe "the title"
        info.description shouldBe "the description"
        info.termsOfService shouldBe "https://any/terms"
        info.version shouldBe "1"
    }

})

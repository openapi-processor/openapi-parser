package io.openapiparser.model.v30

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.openapiparser.support.TestBuilder

class InfoSpec : StringSpec({

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

    "gets contact object" {
        val api = TestBuilder()
            .withApi("""
                openapi: 3.0.3
                info:
                  title: the title
                  contact: {}
                  version: "1"
                paths: {}
            """.trimIndent())
            .buildOpenApi30()

        api.info.contact.shouldNotBeNull()
    }

    "gets license object" {
        val api = TestBuilder()
            .withApi("""
                openapi: 3.0.3
                info:
                  title: the title
                  license: {}
                  version: "1"
                paths: {}
            """.trimIndent())
            .buildOpenApi30()

        api.info.license.shouldNotBeNull()
    }

})

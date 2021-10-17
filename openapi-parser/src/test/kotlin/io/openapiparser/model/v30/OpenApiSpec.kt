package io.openapiparser.model.v30

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
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
                  version: "1"
            """.trimIndent())
            .buildOpenApi30()

        api.info.shouldNotBeNull()
    }

    "gets server objects" {
        val api = TestBuilder()
            .withApi("""
                openapi: 3.0.3
                info:
                  title: the title
                  version: "1"
                servers:
                  - {}
                  - {}
            """.trimIndent())
            .buildOpenApi30()

        api.servers.shouldNotBeNull()
        api.servers.size shouldBe 2
    }

    "gets paths object" {
        val api = TestBuilder()
            .withApi("""
                openapi: 3.0.3
                info:
                  title: the title
                  version: "1"
                paths: {}
            """.trimIndent())
            .buildOpenApi30()

        api.paths.shouldNotBeNull()
    }

})

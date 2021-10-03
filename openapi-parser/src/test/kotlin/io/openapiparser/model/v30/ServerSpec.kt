package io.openapiparser.model.v30

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldExist
import io.kotest.matchers.shouldBe
import io.openapiparser.support.TestBuilder
import io.openapiparser.support.matches

class ServerSpec : StringSpec({

    "gets server objects" {
        val api = TestBuilder()
            .withApi("""
                openapi: 3.0.3
                info:
                  title: title
                  version: "1"
                servers:
                 - url: https://a.url
                   description: a description
                   variables: {}
                 - url: https://b.url
                   description: b description
                   variables: {}
                paths: {}
            """.trimIndent())
            .buildOpenApi30()

        val servers = api.servers
        servers.size shouldBe 2
        servers.shouldExist { it.matches("https://a.url", "a description") }
        servers.shouldExist { it.matches("https://b.url", "b description") }
    }

})

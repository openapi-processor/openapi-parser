package io.openapiparser.model.v30

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.openapiparser.support.TestBuilder
import io.openapiparser.support.matches

class ServerVariableSpec : StringSpec({

    "gets server variables" {
        val api = TestBuilder()
            .withApi("""
                servers:
                - url: https://{one}.{two}/url
                  description: a description
                  variables:
                    one:
                      default: one-default
                      description: one description
                    two:
                      enum:
                        - two-one
                        - two-two
                      default: two-default
                      description: two description
            """.trimIndent())
            .buildOpenApi30()

        val variables = api.servers.first().variables
        variables.size shouldBe 2
        variables["one"]?.matches("one-default", "one description")
        variables["two"]?.matches("two-default", "two description",
            listOf("two-one", "two-two"))
    }

})

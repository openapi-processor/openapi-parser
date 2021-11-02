/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v30

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import io.openapiparser.support.TestBuilder

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
        variables["one"]?.shouldBe("one-default", "one description")
        variables["two"]?.shouldBe("two-default", "two description", listOf("two-one", "two-two"))
    }

})

fun ServerVariable.shouldBe(default: String, description: String) {
    this.default shouldBe default
    this.description shouldBe description
}

fun ServerVariable.shouldBe(default: String, description: String, enum: List<String>) {
    this.default shouldBe default
    this.description shouldBe description
    this.enum shouldContainAll enum
    this.enum.size shouldBe enum.size
}

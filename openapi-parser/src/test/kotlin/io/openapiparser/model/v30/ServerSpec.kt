/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v30

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldExist
import io.kotest.matchers.nulls.shouldNotBeNull
import io.openapiparser.support.TestBuilder
import io.openapiparser.support.matches

class ServerSpec : StringSpec({

    "gets server objects" {
        val api = TestBuilder()
            .withApi("""
                servers:
                 - url: https://a.url
                   description: a description
                   variables: {}
                 - url: https://b.url
                   description: b description
                   variables: {}
            """.trimIndent())
            .buildOpenApi30()

        val servers = api.servers
        servers.shouldExist { it.matches("https://a.url", "a description") }
        servers.shouldExist { it.matches("https://b.url", "b description") }
    }

    "gets server variables" {
        val api = TestBuilder()
            .withApi("""
                servers:
                - url: https://{one}.{two}/url
                  variables: {}
            """.trimIndent())
            .buildOpenApi30()

        val server = api.servers.first()
        server.variables.shouldNotBeNull()
    }

})

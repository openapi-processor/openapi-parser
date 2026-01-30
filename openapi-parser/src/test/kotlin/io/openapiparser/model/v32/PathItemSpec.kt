/*
 * Copyright 2025 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v32

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.maps.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.openapiparser.support.ApiBuilder

class PathItemSpec : StringSpec({

    "gets path item object" {
        val api = ApiBuilder()
            .withApi("""
                paths:
                  /foo:
                    summary: a summary
                    get: {}
                    put: {}
                    post: {}
                    delete: {}
                    options: {}
                    head: {}
                    patch: {}
                    trace: {}
                    query: {}
                    servers:
                      - {}
                      - {}
                    parameters:
                      - {}
                      - {}
            """.trimIndent())
            .buildOpenApi32()

        val path = api.paths?.getPathItem("/foo")!!
        path.isRef.shouldBeFalse()
        path.summary shouldBe "a summary"
        path.get.shouldNotBeNull()
        path.put.shouldNotBeNull()
        path.post.shouldNotBeNull()
        path.delete.shouldNotBeNull()
        path.options.shouldNotBeNull()
        path.head.shouldNotBeNull()
        path.patch.shouldNotBeNull()
        path.trace.shouldNotBeNull()
        path.query.shouldNotBeNull()
        path.servers.shouldNotBeNull()
        path.servers.size shouldBe 2
        path.parameters.shouldNotBeNull()
        path.parameters.size shouldBe 2
    }

    "gets path item parameters with \$ref" {
        val api = ApiBuilder()
            .withApi("""
                paths:
                  /foo:
                    parameters:
                      - ${'$'}ref: '#/parameter'
                parameter: {}
            """.trimIndent())
            .buildOpenApi32()

        val path = api.paths?.getPathItem("/foo")
        val params = path?.parameters
        params?.size shouldBe 1
    }

    "gets \$ref path item object" {
        val api = ApiBuilder()
            .withApi("""
                paths:
                  /foo:
                    ${'$'}ref: '#/path'
                path:
                  summary: a summary
                  description: a description
            """.trimIndent())
            .buildOpenApi32()

        var path = api.paths?.getPathItem("/foo")
        path?.ref shouldBe "#/path"
        path = path?.refObject
        path?.summary shouldBe "a summary"
        path?.description shouldBe "a description"
    }

    "gets operations map in source order" {
        val api = ApiBuilder()
            .withApi("""
                paths:
                  /foo:
                    get: {}
                    put: {}
                    post: {}
                    delete: {}
                    options: {}
                    head: {}
                    patch: {}
                    trace: {}
                    query: {}
            """.trimIndent())
            .buildOpenApi32()

        val path = api.paths?.getPathItem("/foo")

        val operations = path?.operations
        operations?.shouldHaveSize(9)

        operations?.keys.shouldContainExactly(linkedSetOf(
            "get", "put", "post", "delete", "options", "head", "patch", "trace", "query"
        ))
    }

    "get additional operations map" {
        val api = ApiBuilder()
            .withApi("""
                paths:
                  /foo:
                    additionalOperations:
                        one: {}
                        two: {}
            """.trimIndent())
            .buildOpenApi32()

        val path = api.paths?.getPathItem("/foo")

        val operations = path?.additionalOperations
        operations?.shouldHaveSize(2)

        operations?.keys.shouldContainExactly(linkedSetOf(
            "one", "two"
        ))
    }
})

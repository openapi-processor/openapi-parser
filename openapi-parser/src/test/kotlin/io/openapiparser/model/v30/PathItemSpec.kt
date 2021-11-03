/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v30

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.openapiparser.support.TestBuilder

class PathItemSpec : StringSpec({

    "gets path item object" {
        val api = TestBuilder()
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
            """.trimIndent())
            .buildOpenApi30()

        val path = api.paths.getPathItem("/foo")
        path.ref.shouldBeNull()
        path.summary shouldBe "a summary"
        path.get.shouldNotBeNull()
        path.put.shouldNotBeNull()
        path.post.shouldNotBeNull()
        path.delete.shouldNotBeNull()
        path.options.shouldNotBeNull()
        path.head.shouldNotBeNull()
        path.patch.shouldNotBeNull()
        path.trace.shouldNotBeNull()
    }

    "gets \$ref path item object" {
        val api = TestBuilder()
            .withApi("""
                paths:
                  /foo:
                    ${'$'}ref: '#/path'
                path:
                  summary: a summary
                  description: a description
            """.trimIndent())
            .buildOpenApi30()

        val path = api.paths.getPathItem("/foo")
        path.ref shouldBe "#/path"
        path.summary shouldBe "a summary"
        path.description shouldBe "a description"
    }

})

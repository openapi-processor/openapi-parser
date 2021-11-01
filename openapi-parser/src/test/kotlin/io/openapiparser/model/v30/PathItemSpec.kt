/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v30

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.openapiparser.support.TestBuilder

class PathItemSpec : StringSpec({

    "gets \$ref path item object" {
        val api = TestBuilder()
            .withApi("""
                paths:
                  /foo:
                    ${'$'}ref: '#/path'
                path:
                  summary: a summary
            """.trimIndent())
            .buildOpenApi30()

        val path = api.paths.getPathItem("/foo")
        path.ref shouldBe "#/path"
        path.summary shouldBe "a summary"
    }

})

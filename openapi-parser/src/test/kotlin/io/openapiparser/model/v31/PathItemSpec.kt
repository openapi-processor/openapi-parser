package io.openapiparser.model.v31

/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.maps.shouldHaveSize
import io.openapiparser.support.ApiBuilder

class PathItemSpec : StringSpec({

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
            """.trimIndent())
            .buildOpenApi30()

        val path = api.paths.getPathItem("/foo")

        val operations = path?.operations
        operations?.shouldHaveSize(8)

        operations?.keys.shouldContainExactly(linkedSetOf(
            "get", "put", "post", "delete", "options", "head", "patch", "trace"
        ))
    }
})

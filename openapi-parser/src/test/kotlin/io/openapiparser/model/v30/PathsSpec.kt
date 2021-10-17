/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v30

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.openapiparser.support.TestBuilder

class PathsSpec : StringSpec({

    "gets paths object" {
        val api = TestBuilder()
            .withApi("""
                paths:
                  /foo: {}
            """.trimIndent())
            .buildOpenApi30()

        val paths = api.paths
        paths.pathSet().shouldContainExactly("/foo")
        paths.getPathItem("/foo").shouldNotBeNull()
    }

    "gets paths objects" {
        val api = TestBuilder()
            .withApi("""
                paths:
                  /foo: {}
                  /bar: {}
            """.trimIndent())
            .buildOpenApi30()

        val paths = api.paths.pathItems
        paths.size shouldBe 2
        paths["/foo"].shouldNotBeNull()
        paths["/bar"].shouldNotBeNull()
    }

})

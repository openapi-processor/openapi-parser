/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v3x;

import io.kotest.core.datatest.forAll
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.openapiparser.model.v30.pathItem as pathItem30
import io.openapiparser.model.v31.pathItem as pathItem31

class PathItemSpec: StringSpec({

    "get path item \$ref" {
        val item30 = pathItem30("\$ref: '#/path'")
        item30.isRef.shouldBeTrue()
        item30.ref shouldBe "#/path"

        val item31 = pathItem31("\$ref: '#/path'")
        item31.isRef.shouldBeTrue()
        item31.ref shouldBe "#/path"
    }

    "gets path item summary" {
        pathItem30("summary: a summary").summary shouldBe "a summary"
        pathItem31("summary: a summary").summary shouldBe "a summary"
    }

    "gets path item summary is null if missing" {
        pathItem30().summary.shouldBeNull()
        pathItem31().summary.shouldBeNull()
    }

    include(testDescription("path item 30", ::pathItem30) { it.description })
    include(testDescription("path item 31", ::pathItem31) { it.description })
    
    "gets server objects" {
        forAll(
            pathItem30("servers: [{}, {}]").servers,
            pathItem31("servers: [{}, {}]").servers
        ) { servers ->
            servers.shouldNotBeNull()
            servers.size shouldBe 2
        }
    }

    "gets empty server objects if it is missing" {
        pathItem30().servers.shouldBeEmpty()
        pathItem31().servers.shouldBeEmpty()
    }

    include(testExtensions("path item 30", ::pathItem30) { it.extensions })
    include(testExtensions("path item 31", ::pathItem31) { it.extensions })
})


/*
    "gets path item parameters with \$ref" {
        val api = TestBuilder()
            .withApi("""
                paths:
                  /foo:
                    parameters:
                      - ${'$'}ref: '#/parameter'
                parameter: {}
            """.trimIndent())
            .buildOpenApi30()

        val path = api.paths.getPathItem("/foo")
        val params = path?.parameters
        params?.size shouldBe 1
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
        path?.ref shouldBe "#/path"
        path?.summary shouldBe "a summary"
        path?.description shouldBe "a description"
    }
 */

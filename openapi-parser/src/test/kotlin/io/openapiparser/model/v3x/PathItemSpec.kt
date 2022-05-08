/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v3x

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.openapiparser.model.v30.openapi as openapi30
import io.openapiparser.model.v30.pathItem as pathItem30
import io.openapiparser.model.v31.openapi as openapi31
import io.openapiparser.model.v31.pathItem as pathItem31

class PathItemSpec: StringSpec({

    "get path item \$ref" {
        val source = """
            paths:
              /foo:
                ${'$'}ref: '#/path'
            path: {}
        """

        val item30 = openapi30(source).paths.getPathItem("/foo")!!
        item30.isRef.shouldBeTrue()
        item30.ref shouldBe "#/path"

        val item31 = openapi30(source).paths.getPathItem("/foo")!!
        item31.isRef.shouldBeTrue()
        item31.ref shouldBe "#/path"
    }

    "get path item property from \$ref" {
        val source = """
            paths:
              /foo:
                parameters:
                  - ${'$'}ref: '#/parameter'
            parameter:
                name: ref name
        """

        val parameter30 = openapi30(source).paths .getPathItem("/foo")?.parameters?.first()
        parameter30?.refObject?.name shouldBe "ref name"

        val parameter31 = openapi31(source).paths?.getPathItem("/foo")?.parameters?.first()
        parameter31?.refObject?.name shouldBe "ref name"
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

    // todo operations

    "gets path item server objects" {
        val s30 = pathItem30("servers: [{}, {}]").servers
        s30.shouldNotBeNull()
        s30.size shouldBe 2

        val s31 = pathItem31("servers: [{}, {}]").servers
        s31.shouldNotBeNull()
        s31.size shouldBe 2
    }

    "gets path item empty server objects if it is missing" {
        pathItem30().servers.shouldBeEmpty()
        pathItem31().servers.shouldBeEmpty()
    }

    "gets path item parameters" {
        val p30 = pathItem30("parameters: [{}, {}]").parameters
        p30.shouldNotBeNull()
        p30.size shouldBe 2

        val p31 = pathItem31("parameters: [{}, {}]").parameters
        p31.shouldNotBeNull()
        p31.size shouldBe 2
    }

    "gets path item empty parameters objects if it is missing" {
        pathItem30().parameters.shouldBeEmpty()
        pathItem31().parameters.shouldBeEmpty()
    }

    include(testExtensions("path item 30", ::pathItem30) { it.extensions })
    include(testExtensions("path item 31", ::pathItem31) { it.extensions })
})

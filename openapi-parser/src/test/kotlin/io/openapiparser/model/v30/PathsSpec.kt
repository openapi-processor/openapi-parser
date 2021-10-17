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
                openapi: 3.0.3
                info:
                  title: title
                  version: "1"
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
                openapi: 3.0.3
                info:
                  title: title
                  version: "1"
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

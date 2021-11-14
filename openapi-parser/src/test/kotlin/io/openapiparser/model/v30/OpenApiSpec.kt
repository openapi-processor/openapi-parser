/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v30

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.openapiparser.NoValueException
import io.openapiparser.support.buildObject

class OpenApiSpec : StringSpec({

    "gets openapi version" {
        openapi("openapi: 3.0.3").openapi shouldBe "3.0.3"
    }

    "gets openapi version throws if it is missing" {
        shouldThrow<NoValueException> { openapi().openapi }
    }

    "gets info object" {
        openapi("info: {}").info.shouldNotBeNull()
    }

    "gets info object throws if it missing" {
        shouldThrow<NoValueException> { openapi().info }
    }

    "gets server objects" {
        val servers = openapi("servers: [{}, {}]").servers
        servers.shouldNotBeNull()
        servers.size
    }

    "gets empty server objects if it is missing" {
        openapi().servers.shouldBeEmpty()
    }

    "gets paths object" {
        openapi("paths: {}").paths.shouldNotBeNull()
    }

    "gets path object throws if it missing" {
        shouldThrow<NoValueException> { openapi().paths }
    }

    "gets components objects" {
        openapi("""
            components:
              foo: {}
              bar: {}
        """).components.shouldNotBeNull()
    }

    "gets security requirements objects" {
        val security = openapi("security: [{}, {}]").security
        security.shouldNotBeNull()
        security.size shouldBe 2
    }

    "gets empty security requirements objects if it is missing" {
        openapi().security.shouldBeEmpty()
    }

    "gets tags array" {
        val tags = openapi("tags: [{}, {}]").tags
        tags.shouldNotBeEmpty()
        tags.size shouldBe 2
    }

    "gets empty tag objects if it is missing" {
        openapi().tags.shouldBeEmpty()
    }

    "gets external docs object" {
        openapi("externalDocs: {}").externalDocs.shouldNotBeNull()
    }

    "gets external docs object is null if it missing" {
        openapi().externalDocs.shouldBeNull()
    }

    "gets extension values" {
        val extensions = openapi("""
          any: value
          x-foo: "foo extension"
          x-bar: "bar extension"
        """).extensions

        extensions.shouldNotBeNull()
        extensions.size shouldBe 2
    }
})

fun openapi(content: String = "{}"): OpenApi {
    return buildObject(OpenApi::class.java, content)
}

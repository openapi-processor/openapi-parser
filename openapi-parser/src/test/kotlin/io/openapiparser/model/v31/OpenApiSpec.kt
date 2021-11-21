/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v31

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.openapiparser.NoValueException
import io.openapiparser.support.TestBuilder
import io.openapiparser.support.buildObject

class OpenApiSpec: StringSpec({

    "gets openapi version" {
        openapi("openapi: 3.1.0").openapi shouldBe "3.1.0"
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

    "gets json schema dialect" {
        openapi("jsonSchemaDialect: https://schema.uri").jsonSchemaDialect.shouldNotBeNull()
    }

    "gets null json schema dialect if it missing" {
        openapi().jsonSchemaDialect.shouldBeNull()
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

    "gets path object is null if it missing" {
        openapi().paths.shouldBeNull()
    }

    "gets webhooks objects" {
        val webhooks = openapi("""
            webhooks:
              /foo: {}
              /bar: {}
        """).webhooks

        webhooks.size shouldBe 2
        webhooks["/foo"].shouldNotBeNull()
        webhooks["/bar"].shouldNotBeNull()
    }

    "gets webhooks is null if it missing" {
        val api = TestBuilder()
            .withApi("""
                any: value
            """.trimIndent())
            .buildOpenApi31()

        api.webhooks.shouldBeNull()
    }

    "gets components objects" {
        val api = TestBuilder()
            .withApi("""
                components:
                  foo: "foo"
                  bar: "bar"
            """.trimIndent())
            .buildOpenApi31()

        val components = api.components
        components.shouldNotBeNull()
    }

    "gets security requirements objects" {
        val api = TestBuilder()
            .withApi("""
                security:
                  - {}
                  - {}
            """.trimIndent())
            .buildOpenApi31()

        val security = api.security
        security.shouldNotBeNull()
        security.size shouldBe 2
    }

    "gets empty security requirements objects if it is missing" {
        val api = TestBuilder()
            .withApi("""
                any: value
            """.trimIndent())
            .buildOpenApi31()

        val security = api.security
        security.shouldBeEmpty()
    }

    "gets tags array" {
        val api = TestBuilder()
            .withApi("""
                tags:
                  - {}
                  - {}
            """.trimIndent())
            .buildOpenApi31()

        val tags = api.tags
        tags.shouldNotBeEmpty()
        tags.size shouldBe 2
    }

    "gets empty tag objects if it is missing" {
        val api = TestBuilder()
            .withApi("""
                any: value
            """.trimIndent())
            .buildOpenApi31()

        api.tags.shouldBeEmpty()
    }

    "gets external docs object" {
        val api = TestBuilder()
            .withApi("""
                externalDocs: {}
            """.trimIndent())
            .buildOpenApi31()

        api.externalDocs.shouldNotBeNull()
    }

    "gets external docs object is null if it missing" {
        val api = TestBuilder()
            .withApi("""
                any: value
            """.trimIndent())
            .buildOpenApi31()

        api.externalDocs.shouldBeNull()
    }

    "gets extension values" {
        val api = TestBuilder()
            .withApi("""
                any: value
                x-foo: "foo extension"
                x-bar: "bar extension"
            """.trimIndent())
            .buildOpenApi31()

        val extensions = api.extensions
        extensions.shouldNotBeNull()
        extensions.size shouldBe 2
    }
})

fun openapi(content: String = "{}"): OpenApi {
    return buildObject(OpenApi::class.java, content)
}

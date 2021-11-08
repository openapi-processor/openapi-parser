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
import io.openapiparser.support.TestBuilder

class OpenApiSpec : StringSpec({

    "gets openapi version" {
        val api = TestBuilder()
            .withApi("""
                openapi: 3.0.3
            """.trimIndent())
            .buildOpenApi30()

        api.openapi shouldBe "3.0.3"
    }

    "gets openapi version throws if it is missing" {
        val api = TestBuilder()
            .withApi("""
                any: value
            """.trimIndent())
            .buildOpenApi30()

        shouldThrow<NoValueException> {
            api.openapi
        }
    }

    "gets info object" {
        val api = TestBuilder()
            .withApi("""
                info: {}
            """.trimIndent())
            .buildOpenApi30()

        api.info.shouldNotBeNull()
    }

    "gets info object throws if it missing" {
        val api = TestBuilder()
            .withApi("""
                any: value
            """.trimIndent())
            .buildOpenApi30()

        shouldThrow<NoValueException> {
            api.info
        }
    }

    "gets server objects" {
        val api = TestBuilder()
            .withApi("""
                servers:
                  - {}
                  - {}
            """.trimIndent())
            .buildOpenApi30()

        val servers = api.servers
        servers.shouldNotBeNull()
        servers.size shouldBe 2
    }

    "gets empty server objects if it is missing" {
        val api = TestBuilder()
            .withApi("""
                openapi: 3.0.3
            """.trimIndent())
            .buildOpenApi30()

        val servers = api.servers
        servers.shouldBeEmpty()
    }

    "gets paths object" {
        val api = TestBuilder()
            .withApi(
                """
                paths: {}
            """.trimIndent()
            )
            .buildOpenApi30()

        api.paths.shouldNotBeNull()
    }

    "gets path object throws if it missing" {
        val api = TestBuilder()
            .withApi("""
                any: value
            """.trimIndent())
            .buildOpenApi30()

        shouldThrow<NoValueException> {
            api.paths
        }
    }

    "gets components objects" {
        val api = TestBuilder()
            .withApi("""
                components:
                  foo: "foo"
                  bar: "bar"
            """.trimIndent())
            .buildOpenApi30()

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
            .buildOpenApi30()

        val security = api.security
        security.shouldNotBeNull()
        security.size shouldBe 2
    }

    "gets empty security requirements objects if it is missing" {
        val api = TestBuilder()
            .withApi("""
                any: value
            """.trimIndent())
            .buildOpenApi30()

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
            .buildOpenApi30()

        val tags = api.tags
        tags.shouldNotBeEmpty()
        tags.size shouldBe 2
    }

    "gets empty tag objects if it is missing" {
        val api = TestBuilder()
            .withApi("""
                any: value
            """.trimIndent())
            .buildOpenApi30()

        api.tags.shouldBeEmpty()
    }

    "gets external docs object" {
        val api = TestBuilder()
            .withApi("""
                externalDocs: {}
            """.trimIndent())
            .buildOpenApi30()

        api.externalDocs.shouldNotBeNull()
    }

    "gets external docs object is null if it missing" {
        val api = TestBuilder()
            .withApi("""
                any: value
            """.trimIndent())
            .buildOpenApi30()

        api.externalDocs.shouldBeNull()
    }

    "gets extension values" {
        val api = TestBuilder()
            .withApi("""
                any: value
                x-foo: "foo extension"
                x-bar: "bar extension"
            """.trimIndent())
            .buildOpenApi30()

        val extensions = api.extensions
        extensions.shouldNotBeNull()
        extensions.size shouldBe 2
    }
})

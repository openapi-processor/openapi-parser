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

class OpenApiSpec  : StringSpec({

//    "gets info object" {
//        val api = TestBuilder()
//            .withApi(
//                """
//                openapi: 3.1.0
//                info:
//                  title: the title
//                  summary: summary
//                  description: the description
//                  termsOfService: https://any/terms
//                  version: "1"
//            """.trimIndent()
//            )
//            .buildOpenApi31()
//
//        val info = api.info
//        info.title shouldBe "the title"
//        info.summary shouldBe "summary"
//        info.description shouldBe "the description"
//        info.termsOfService shouldBe "https://any/terms"
//        info.version shouldBe "1"
//    }

    "gets openapi version" {
        val api = TestBuilder()
            .withApi("""
                openapi: 3.1.0
            """.trimIndent())
            .buildOpenApi31()

        api.openapi shouldBe "3.1.0"
    }

    "gets openapi version throws if it is missing" {
        val api = TestBuilder()
            .withApi("""
                any: value
            """.trimIndent())
            .buildOpenApi31()

        shouldThrow<NoValueException> {
            api.openapi
        }
    }

    "gets info object" {
        val api = TestBuilder()
            .withApi("""
                info: {}
            """.trimIndent())
            .buildOpenApi31()

        api.info.shouldNotBeNull()
    }

    "gets info object throws if it missing" {
        val api = TestBuilder()
            .withApi("""
                any: value
            """.trimIndent())
            .buildOpenApi31()

        shouldThrow<NoValueException> {
            api.info
        }
    }

    "gets json schema dialect" {
        val api = TestBuilder()
            .withApi("""
                jsonSchemaDialect: https://schema.uri
            """.trimIndent())
            .buildOpenApi31()

        api.jsonSchemaDialect.shouldNotBeNull()
    }

    "gets null json schema dialect if it missing" {
        val api = TestBuilder()
            .withApi("""
                any: value
            """.trimIndent())
            .buildOpenApi31()

        api.externalDocs.shouldBeNull()
    }

    "gets server objects" {
        val api = TestBuilder()
            .withApi("""
                servers:
                  - {}
                  - {}
            """.trimIndent())
            .buildOpenApi31()

        val servers = api.servers
        servers.shouldNotBeNull()
        servers.size shouldBe 2
    }

    "gets empty server objects if it is missing" {
        val api = TestBuilder()
            .withApi("""
                openapi: 3.1.0
            """.trimIndent())
            .buildOpenApi31()

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
            .buildOpenApi31()

        api.paths.shouldNotBeNull()
    }

    "gets path object is null if it missing" {
        val api = TestBuilder()
            .withApi("""
                any: value
            """.trimIndent())
            .buildOpenApi31()

        api.paths.shouldBeNull()
    }

    "gets webhooks objects" {
        val api = TestBuilder()
            .withApi("""
                webhooks:
                  /foo: {}
                  /bar: {}
            """.trimIndent())
            .buildOpenApi31()

        val webhooks = api.webhooks
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

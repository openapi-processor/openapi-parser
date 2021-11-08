/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v30

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.openapiparser.NoValueException
import io.openapiparser.support.TestBuilder

class InfoSpec : StringSpec({

    "gets info title" {
        val api = TestBuilder()
            .withApi("""
                info:
                  title: the title
            """.trimIndent())
            .buildOpenApi30()

        val info = api.info
        info.title shouldBe "the title"
    }

    "gets info title throws if it is missing" {
        val api = TestBuilder()
            .withApi("""
                info: {}
            """.trimIndent())
            .buildOpenApi30()

        shouldThrow<NoValueException> {
            api.info.title
        }
    }

    "gets info version" {
        val api = TestBuilder()
            .withApi("""
                info:
                  version: "1"
            """.trimIndent())
            .buildOpenApi30()

        val info = api.info
        info.version shouldBe "1"
    }

    "gets info version throws if it is missing" {
        val api = TestBuilder()
            .withApi("""
                info: {}
            """.trimIndent())
            .buildOpenApi30()

        shouldThrow<NoValueException> {
            api.info.version
        }
    }

    "gets info description" {
        val api = TestBuilder()
            .withApi("""
                info:
                  description: the description
            """.trimIndent())
            .buildOpenApi30()

        val info = api.info
        info.description shouldBe "the description"
    }

    "gets info description is null if missing" {
        val api = TestBuilder()
            .withApi("""
                info: {}
            """.trimIndent())
            .buildOpenApi30()

        val info = api.info
        info.description.shouldBeNull()
    }

    "gets info terms of service" {
        val api = TestBuilder()
            .withApi("""
                info:
                  termsOfService: https://any/terms
            """.trimIndent())
            .buildOpenApi30()

        val info = api.info
        info.termsOfService shouldBe "https://any/terms"
    }

    "gets info terms of service is null if missing" {
        val api = TestBuilder()
            .withApi("""
                info: {}
            """.trimIndent())
            .buildOpenApi30()

        val info = api.info
        info.termsOfService.shouldBeNull()
    }

    "gets contact object" {
        val api = TestBuilder()
            .withApi("""
                info:
                  contact: {}
            """.trimIndent())
            .buildOpenApi30()

        api.info.contact.shouldNotBeNull()
    }

    "gets contact object is null if missing" {
        val api = TestBuilder()
            .withApi("""
                info: {}
            """.trimIndent())
            .buildOpenApi30()

        api.info.contact.shouldBeNull()
    }

    "gets license object" {
        val api = TestBuilder()
            .withApi("""
                info:
                  license: {}
            """.trimIndent())
            .buildOpenApi30()

        api.info.license.shouldNotBeNull()
    }

    "gets license object is null if missing" {
        val api = TestBuilder()
            .withApi("""
                info: {}
            """.trimIndent())
            .buildOpenApi30()

        api.info.license.shouldBeNull()
    }
})

/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v31

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.openapiparser.support.TestBuilder

class ContactSpec : StringSpec({

    "gets contact name" {
        val info = TestBuilder()
            .withApi("""
                  contact:
                    name: contact name
            """.trimIndent())
            .build(Info::class.java)

        val contact = info.contact
        contact.name shouldBe "contact name"
    }

    "gets contact name is null if missing" {
        val info = TestBuilder()
            .withApi("""
                  contact: {}
            """.trimIndent())
            .build(Info::class.java)

        val contact = info.contact
        contact.name.shouldBeNull()
    }

    "gets contact url" {
        val info = TestBuilder()
            .withApi("""
                  contact:
                    url: https://contact.url
            """.trimIndent())
            .build(Info::class.java)

        val contact = info.contact
        contact.url shouldBe "https://contact.url"
    }

    "gets contact url is null if missing" {
        val info = TestBuilder()
            .withApi("""
                  contact: {}
            """.trimIndent())
            .build(Info::class.java)

        val contact = info.contact
        contact.url.shouldBeNull()
    }

    "gets contact email" {
        val info = TestBuilder()
            .withApi("""
                  contact:
                    email: contact@email
            """.trimIndent())
            .build(Info::class.java)

        val contact = info.contact
        contact.email shouldBe "contact@email"
    }

    "gets contact email is null if missing" {
        val info = TestBuilder()
            .withApi("""
                  contact: {}
            """.trimIndent())
            .build(Info::class.java)

        val contact = info.contact
        contact.email.shouldBeNull()
    }

    "gets extension values" {
        val info = TestBuilder()
            .withApi("""
                contact:
                  x-foo: "foo extension"
                  x-bar: "bar extension"
            """.trimIndent())
            .build(Info::class.java)

        val extensions = info.contact.extensions
        extensions.shouldNotBeNull()
        extensions.size shouldBe 2
    }
})

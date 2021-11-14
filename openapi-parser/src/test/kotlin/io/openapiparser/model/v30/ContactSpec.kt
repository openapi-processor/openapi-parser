/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v30

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.openapiparser.support.buildObject

class ContactSpec : StringSpec({

    "gets contact name" {
        contact("name: contact name").name shouldBe "contact name"
    }

    "gets contact name is null if missing" {
        contact().name.shouldBeNull()
    }

    "gets contact url" {
        contact("url: https://contact.url").url shouldBe "https://contact.url"
    }

    "gets contact url is null if missing" {
        contact().url.shouldBeNull()
    }

    "gets contact email" {
        contact("email: contact@email").email shouldBe "contact@email"
    }

    "gets contact email is null if missing" {
        contact().email.shouldBeNull()
    }

    "gets extension values" {
        val extensions = contact("""
          x-foo: "foo extension"
          x-bar: "bar extension"
        """).extensions

        extensions.shouldNotBeNull()
        extensions.size shouldBe 2
    }
})

fun contact(content: String = "{}"): Contact {
    return buildObject(Contact::class.java, content)
}

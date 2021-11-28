/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v3x

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.openapiparser.model.v30.contact as contact30
import io.openapiparser.model.v31.contact as contact31

class ContactSpec: StringSpec({

    "gets contact name" {
        contact30("name: contact name").name shouldBe "contact name"
        contact31("name: contact name").name shouldBe "contact name"
    }

    "gets contact name is null if missing" {
        contact30().name.shouldBeNull()
        contact31().name.shouldBeNull()
    }

    "gets contact url" {
        contact30("url: https://contact.url").url shouldBe "https://contact.url"
        contact31("url: https://contact.url").url shouldBe "https://contact.url"
    }

    "gets contact url is null if missing" {
        contact30().url.shouldBeNull()
        contact31().url.shouldBeNull()
    }

    "gets contact email" {
        contact30("email: contact@email").email shouldBe "contact@email"
        contact31("email: contact@email").email shouldBe "contact@email"
    }

    "gets contact email is null if missing" {
        contact30().email.shouldBeNull()
        contact31().email.shouldBeNull()
    }

    include(testExtensions("Contact30", ::contact30) { it.extensions })
    include(testExtensions("Contact31", ::contact31) { it.extensions })
})

/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v3x

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.openapiparser.NoValueException
import io.openapiparser.model.v30.info as info30
import io.openapiparser.model.v31.info as info31

class InfoSpec: StringSpec({

    "gets info title" {
        info30("title: the title").title shouldBe "the title"
        info31("title: the title").title shouldBe "the title"
    }

    "gets info title throws if it is missing" {
        shouldThrow<NoValueException> { info30().title }
        shouldThrow<NoValueException> { info31().title }
    }

    "gets info version" {
        info30("""version: "1"""").version shouldBe "1"
        info31("""version: "1"""").version shouldBe "1"
    }

    "gets info version throws if it is missing" {
        shouldThrow<NoValueException> { info30().version }
        shouldThrow<NoValueException> { info31().version }
    }

    include(testDescription("info 30", ::info30) { it.description })
    include(testDescription("info 31", ::info31) { it.description })

    "gets info terms of service" {
        info30("termsOfService: https://any/terms").termsOfService shouldBe "https://any/terms"
        info31("termsOfService: https://any/terms").termsOfService shouldBe "https://any/terms"
    }

    "gets info terms of service is null if missing" {
        info30().termsOfService.shouldBeNull()
        info31().termsOfService.shouldBeNull()
    }

    "gets contact object" {
        info30("contact: {}").contact.shouldNotBeNull()
        info31("contact: {}").contact.shouldNotBeNull()
    }

    "gets contact object is null if missing" {
        info30().contact.shouldBeNull()
        info31().contact.shouldBeNull()
    }

    "gets license object" {
        info30("license: {}").license.shouldNotBeNull()
        info31("license: {}").license.shouldNotBeNull()
    }

    "gets license object is null if missing" {
        info30().license.shouldBeNull()
        info31().license.shouldBeNull()
    }

    include(testExtensions("Info30", ::info30) { it.extensions })
    include(testExtensions("Info31", ::info30) { it.extensions })
})

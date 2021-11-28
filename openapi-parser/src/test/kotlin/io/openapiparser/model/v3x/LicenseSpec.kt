/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v3x

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.openapiparser.NoValueException
import io.openapiparser.model.v30.license as license30
import io.openapiparser.model.v31.license as license31

class LicenseSpec: StringSpec({

    "gets license name" {
        license30("name: license name").name shouldBe "license name"
        license31("name: license name").name shouldBe "license name"
    }

    "gets license name throws if it missing" {
        shouldThrow<NoValueException> { license30().name }
        shouldThrow<NoValueException> { license31().name }
    }

    "gets license url" {
        license30("url: https://license").url shouldBe "https://license"
        license31("url: https://license").url shouldBe "https://license"
    }

    "gets license url is null if missing" {
        license30().url.shouldBeNull()
        license31().url.shouldBeNull()
    }

    include(testExtensions("License30", ::license30) { it.extensions })
    include(testExtensions("License31", ::license31) { it.extensions })
})

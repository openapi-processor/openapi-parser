/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v31

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.openapiparser.NoValueException
import io.openapiparser.support.TestBuilder

class LicenseSpec : StringSpec({

    "gets license object" {
        val info = TestBuilder()
            .withApi("""
              license:
                name: license name
                identifier: Apache-2.0
                url: https://license
            """.trimIndent())
            .build(Info::class.java)

        val license = info.license
        license.name shouldBe "license name"
        license.identifier shouldBe "Apache-2.0"
        license.url shouldBe "https://license"
    }

    "gets name throws if it missing" {
        val license = TestBuilder()
            .withApi("""
                any: value
            """.trimIndent())
            .build(License::class.java)

        shouldThrow<NoValueException> {
            license.name
        }
    }

    "gets url is null if missing" {
        val info = TestBuilder()
            .withApi("""
                license: {}
            """.trimIndent())
            .build(License::class.java)

        val license = listOf(info)
        info.url.shouldBeNull()
    }

    "gets identifier is null if missing" {
        val license = TestBuilder()
            .withApi("""
                {}
            """.trimIndent())
            .build(License::class.java)

        license.identifier.shouldBeNull()
    }

    "gets extension values" {
        val license = TestBuilder()
            .withApi("""
              x-foo: "foo extension"
              x-bar: "bar extension"
            """.trimIndent())
            .build(License::class.java)

        val extensions = license.extensions
        extensions.shouldNotBeNull()
        extensions.size shouldBe 2
    }

})

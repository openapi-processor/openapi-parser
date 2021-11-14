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
import io.openapiparser.support.buildObject

class LicenseSpec : StringSpec({

    "gets license object" {
        val license = license("""
          name: license name
          url: https://license
          """)

        license.name shouldBe "license name"
        license.url shouldBe "https://license"
    }

    "gets name throws if it missing" {
        shouldThrow<NoValueException> {
            license().name
        }
    }

    "gets url is null if missing" {
        license().url.shouldBeNull()
    }

    "gets extension values" {
        val license = license("""
          x-foo: "foo extension"
          x-bar: "bar extension"
        """)

        val extensions = license.extensions
        extensions.shouldNotBeNull()
        extensions.size shouldBe 2
    }

})

fun license(content: String = "{}"): License {
    return buildObject(License::class.java, content)
}

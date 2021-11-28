/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v30

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.openapiparser.NoValueException
import io.openapiparser.support.buildObject

class OpenApiSpec : StringSpec({

    "gets paths object" {
        openapi("paths: {}").paths.shouldNotBeNull()
    }

    "gets path object throws if it missing" {
        shouldThrow<NoValueException> { openapi().paths }
    }

    "gets extension values" {
        val extensions = openapi("""
          any: value
          x-foo: "foo extension"
          x-bar: "bar extension"
        """).extensions

        extensions.shouldNotBeNull()
        extensions.size shouldBe 2
    }
})

fun openapi(content: String = "{}"): OpenApi {
    return buildObject(OpenApi::class.java, content)
}

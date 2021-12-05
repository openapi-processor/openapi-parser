/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v3x

import io.kotest.core.spec.style.stringSpec
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

fun <O> testExtensions(
    name: String,
    build: (content: String) -> O,
    extract: (o: O) -> Map<String, Any>
) = stringSpec {

        "gets $name extension values" {
            val source = """
          any: value
          x-foo: "foo extension"
          x-bar: "bar extension"
        """

            val extensions = extract(build(source))
            extensions.shouldNotBeNull()
            extensions.size shouldBe 2
        }

        "gets $name empty extension values" {
            extract(build("{}")).shouldBeEmpty()
        }
    }

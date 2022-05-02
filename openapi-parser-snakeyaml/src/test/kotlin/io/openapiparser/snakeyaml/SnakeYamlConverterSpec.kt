/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.snakeyaml

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.openapiparser.ConverterException
import io.openapiparser.snakeyaml.support.convertToMap

class SnakeYamlConverterSpec : StringSpec ({

    "throws on empty input" {
        val converter = SnakeYamlConverter()

        val exception = shouldThrow<ConverterException> {
            converter.convert("")
        }

        exception.message shouldContain "empty"
    }

    "converts yaml input" {
        val converter = SnakeYamlConverter()

        val result = converter.convertToMap(
            """foo: bar"""
        )

        result.size shouldBe 1
        result["foo"] shouldBe "bar"
    }

    "converts yaml input with leading whitespace" {
        val converter = SnakeYamlConverter()

        val result = converter.convertToMap(
            """
                
                foo: bar
            """.trimIndent()
        )

        result.size shouldBe 1
        result["foo"] shouldBe "bar"
    }
})

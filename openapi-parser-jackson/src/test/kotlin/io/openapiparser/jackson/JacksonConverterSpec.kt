/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.jackson

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.openapiparser.ConverterException

class JacksonConverterSpec : StringSpec({

    "throws on null input" {
        val converter = JacksonConverter()

        val exception = shouldThrow<ConverterException> {
            converter.convert(null)
        }

        exception.message shouldContain "empty"
    }

    "throws on empty input" {
        val converter = JacksonConverter()

        val exception = shouldThrow<ConverterException> {
            converter.convert("")
        }

        exception.message shouldContain "empty"
    }

    "converts json object input" {
        val converter = JacksonConverter()

        val result = converter.convert(
            """{ "foo": "bar" }"""
        )

        result.countProperties shouldBe 1
        result["foo"] shouldBe "bar"
    }

    "converts json object input with leading whitespace" {
        val converter = JacksonConverter()

        val result = converter.convert(
            """
                
                { "foo": "bar" }
            """.trimIndent()
        )

        result.countProperties shouldBe 1
        result["foo"] shouldBe "bar"
    }

    "throws on bad json input" {
        val converter = JacksonConverter()

        val exception = shouldThrow<ConverterException> {
            converter.convert(
                """
                {
                    foo
                """.trimIndent()
            )
        }

        exception.message shouldContain "json"
    }

    "converts yaml input" {
        val converter = JacksonConverter()

        val result = converter.convert(
            """foo: bar"""
        )

        result.countProperties shouldBe 1
        result["foo"] shouldBe "bar"
    }

    "converts yaml input with leading whitespace" {
        val converter = JacksonConverter()

        val result = converter.convert(
            """
                
                foo: bar
            """.trimIndent()
        )

        result.countProperties shouldBe 1
        result["foo"] shouldBe "bar"
    }

    "throws on bad yaml input" {
        val converter = JacksonConverter()

        val exception = shouldThrow<ConverterException> {
            converter.convert(
                """
                    foo 
                """.trimIndent()
            )
        }

        exception.message shouldContain "yaml"
    }

})

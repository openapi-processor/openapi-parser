/*
 * Copyright 2023 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.support

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.kotest.matchers.types.shouldNotBeSameInstanceAs
import io.openapiprocessor.jsonschema.support.Copy
import java.math.BigDecimal
import java.math.BigInteger

class CopySpec : StringSpec({

    "deep copies null" {
        Copy.deep(null).shouldBeNull()
    }

    "deep copies String" {
        val source = "a string"
        val result = Copy.deep(source)

        result shouldBe source
        result shouldBeSameInstanceAs source // immutable
    }

    "deep copies Boolean" {
        val source = java.lang.Boolean.valueOf("100")
        val result = Copy.deep(source)

        result shouldBe source
        result shouldBeSameInstanceAs source // immutable
    }

    "deep copies Integer" {
        val source = java.lang.Integer.valueOf("100")
        val result = Copy.deep(source)

        result shouldBe source
        result shouldBeSameInstanceAs source // immutable
    }

    "deep copies Long" {
        val source = java.lang.Long.valueOf("100")
        val result = Copy.deep(source)

        result shouldBe source
        result shouldBeSameInstanceAs source // immutable
    }

    "deep copies Float" {
        val source = java.lang.Float.valueOf("100")
        val result = Copy.deep(source)

        result shouldBe source
        result shouldBeSameInstanceAs source // immutable
    }

    "deep copies Double" {
        val source = java.lang.Double.valueOf("100")
        val result = Copy.deep(source)

        result shouldBe source
        result shouldBeSameInstanceAs source // immutable
    }

    "deep copies BigDecimal" {
        val source = BigDecimal("100.101")
        val result = Copy.deep(source)

        result shouldBe source
        result shouldBeSameInstanceAs source // immutable
    }

    "deep copies BigInteger" {
        val source = BigInteger("100")
        val result = Copy.deep(source)

        result shouldBe source
        result shouldBeSameInstanceAs source // immutable
    }

    "deep copies Collection" {
        val source = listOf("100", "101")
        val result = Copy.deep(source)

        result shouldBe source
        result shouldNotBeSameInstanceAs source
    }

    "deep copies Map" {
        val source = mapOf("100" to "101")
        val result = Copy.deep(source)

        result shouldBe source
        result shouldNotBeSameInstanceAs source
    }
})

/*
 * Copyright 2025 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v32

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf

class EncodingSpec: StringSpec({

    "gets encoding" {
        val encoding = encoding("""
                encoding:
                  Foo: {}
                  Bar: {}
                """
        ).encoding

        encoding.shouldNotBeNull()
        encoding.size shouldBe 2
        encoding["Foo"].shouldBeInstanceOf<Encoding>()
        encoding["Bar"].shouldBeInstanceOf<Encoding>()
    }

    "gets encoding is empty if missing" {
        encoding().encoding.shouldBeEmpty()
    }

    "gets prefixEncoding" {
        val encoding = encoding("""
                prefixEncoding:
                  - {}
                  - {}
                """
        ).prefixEncoding

        encoding.shouldNotBeNull()
        encoding.size shouldBe 2
        encoding.toList()[0].shouldBeInstanceOf<Encoding>()
        encoding.toList()[1].shouldBeInstanceOf<Encoding>()
    }

    "gets prefixEncoding is empty if missing" {
        encoding().prefixEncoding.shouldBeEmpty()
    }

    "gets encoding itemEncoding" {
        val source = """
            itemEncoding: {}
        """

        val encoding = encoding(source).itemEncoding

        encoding.shouldNotBeNull()
        encoding.shouldBeInstanceOf<Encoding>()
    }

    "gets encoding itemEncoding is empty if missing" {
        encoding().itemEncoding.shouldBeNull()
    }
})

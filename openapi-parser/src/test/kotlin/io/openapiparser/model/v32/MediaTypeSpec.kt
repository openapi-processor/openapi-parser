/*
 * Copyright 2025 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v32

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf

class MediaTypeSpec: StringSpec({

    "gets media type item schema" {
        mediaType("itemSchema: {}").itemSchema.shouldNotBeNull()
    }

    "gets media type itemSchema is null if missing" {
        mediaType().itemSchema.shouldBeNull()
    }

    "gets media type prefixEncoding" {
        val source = """
            prefixEncoding:
             - {}
             - {}
        """

        val encoding = mediaType(source).prefixEncoding

        encoding.shouldNotBeNull()
        encoding.size shouldBe 2
        encoding.toList()[0].shouldBeInstanceOf<Encoding>()
        encoding.toList()[1].shouldBeInstanceOf<Encoding>()
    }

    "gets media type prefixEncoding is empty if missing" {
        mediaType().prefixEncoding.shouldBeEmpty()
    }

    "gets media type itemEncoding" {
        val source = """
            itemEncoding: {}
        """

        val encoding = mediaType(source).itemEncoding

        encoding.shouldNotBeNull()
        encoding.shouldBeInstanceOf<Encoding>()
    }

    "gets media type itemEncoding is empty if missing" {
        mediaType().itemEncoding.shouldBeNull()
    }
})

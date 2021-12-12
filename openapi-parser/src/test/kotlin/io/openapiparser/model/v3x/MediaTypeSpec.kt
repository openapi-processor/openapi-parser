/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v3x

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.openapiparser.model.v30.Example as Example30
import io.openapiparser.model.v30.MediaType as MediaType30
import io.openapiparser.model.v30.mediaType as mediaType30
import io.openapiparser.model.v31.Example as Example31
import io.openapiparser.model.v31.MediaType as MediaType31
import io.openapiparser.model.v31.mediaType as mediaType31

class MediaTypeSpec: StringSpec({

    "gets media type schema" {
        mediaType30("schema: {}").schema.shouldNotBeNull()
        mediaType31("schema: {}").schema.shouldNotBeNull()
    }

    "gets media type schema is null if missing" {
        mediaType30().schema.shouldBeNull()
        mediaType31().schema.shouldBeNull()
    }

    "gets media type example" {
        mediaType30("example: {}").example.shouldNotBeNull()
        mediaType31("example: {}").example.shouldNotBeNull()
    }

    "gets media type example is null if missing" {
        mediaType30().example.shouldBeNull()
        mediaType31().example.shouldBeNull()
    }

    "gets media type examples 30" {
        val source = """
            examples:
             foo: {}
             bar: {}
        """

        val examples = mediaType30(source).examples

        examples.shouldNotBeNull()
        examples.size shouldBe 2
        examples["foo"].shouldBeInstanceOf<Example30>()
        examples["bar"].shouldBeInstanceOf<Example30>()
    }

    "gets media type examples 31" {
        val source = """
            examples:
             foo: {}
             bar: {}
        """

        val examples = mediaType31(source).examples

        examples.shouldNotBeNull()
        examples.size shouldBe 2
        examples["foo"].shouldBeInstanceOf<Example31>()
        examples["bar"].shouldBeInstanceOf<Example31>()
    }

    "gets parameter examples is empty if missing" {
        mediaType30().examples.shouldBeEmpty()
        mediaType31().examples.shouldBeEmpty()
    }

    "gets media type content 30" {
        val source = """
            content:
             application/json: {}
             application/xml: {}
        """

        val content = mediaType30(source).content

        content.shouldNotBeNull()
        content.size shouldBe 2
        content["application/json"].shouldBeInstanceOf<MediaType30>()
        content["application/xml"].shouldBeInstanceOf<MediaType30>()
    }

    "gets media type content 31" {
        val source = """
            content:
             application/json: {}
             application/xml: {}
        """

        val content = mediaType31(source).content

        content.shouldNotBeNull()
        content.size shouldBe 2
        content["application/json"].shouldBeInstanceOf<MediaType31>()
        content["application/xml"].shouldBeInstanceOf<MediaType31>()
    }

    "gets media type content is empty if missing" {
        mediaType30().content.shouldBeEmpty()
        mediaType31().content.shouldBeEmpty()
    }

    include(testExtensions("mediaType30", ::mediaType30) { it.extensions })
    include(testExtensions("mediaType31", ::mediaType31) { it.extensions })
})

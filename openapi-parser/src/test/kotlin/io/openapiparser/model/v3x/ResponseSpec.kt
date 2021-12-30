/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v3x

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.datatest.withData
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.openapiparser.NoValueException
import io.openapiparser.model.v30.MediaType as MediaType30
import io.openapiparser.model.v30.response as response30
import io.openapiparser.model.v31.MediaType as MediaType31
import io.openapiparser.model.v31.response as response31

class ResponseSpec: StringSpec({

    "gets response description" {
        response30("description: description").description shouldBe "description"
        response31("description: description").description shouldBe "description"
    }

    "gets response description throws if it is missing" {
        shouldThrow<NoValueException> { response30().description }
        shouldThrow<NoValueException> { response31().description }
    }

    "gets response headers" {
        val source = """
          headers:
            foo: {}
            bar: {}
        """

        withData(
            response30(source).headers,
            response31(source).headers
        ) { headers ->
            headers.size shouldBe 2
            headers["foo"].shouldNotBeNull()
            headers["bar"].shouldNotBeNull()
        }
    }

    "gets response headers is empty if missing" {
        response30().headers.shouldBeEmpty()
        response31().headers.shouldBeEmpty()
    }

    "gets response content 30" {
        val source = """
            content:
             application/json: {}
             application/xml: {}
        """

        val content = response30(source).content

        content.shouldNotBeNull()
        content.size shouldBe 2
        content["application/json"].shouldBeInstanceOf<MediaType30>()
        content["application/xml"].shouldBeInstanceOf<MediaType30>()
    }

    "gets response content 31" {
        val source = """
            content:
             application/json: {}
             application/xml: {}
        """

        val content = response31(source).content

        content.shouldNotBeNull()
        content.size shouldBe 2
        content["application/json"].shouldBeInstanceOf<MediaType31>()
        content["application/xml"].shouldBeInstanceOf<MediaType31>()
    }

    "gets response content is empty if missing" {
        response30().content.shouldBeEmpty()
        response31().content.shouldBeEmpty()
    }

    "gets response links" {
        val source = """
          links:
            foo: {}
            bar: {}
        """

        withData(
            response30(source).links,
            response31(source).links
        ) { links ->
            links.size shouldBe 2
            links["foo"].shouldNotBeNull()
            links["bar"].shouldNotBeNull()
        }
    }

    "gets response links is empty if missing" {
        response30().links.shouldBeEmpty()
        response31().links.shouldBeEmpty()
    }

    include(testExtensions("response 30", ::response30) { it.extensions })
    include(testExtensions("response 31", ::response31) { it.extensions })
})

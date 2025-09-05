/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v3x

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.openapiparser.model.v30.MediaType as MediaType30
import io.openapiparser.model.v30.requestBody as requestBody30
import io.openapiparser.model.v31.MediaType as MediaType31
import io.openapiparser.model.v31.requestBody as requestBody31
import io.openapiparser.model.v32.MediaType as MediaType32
import io.openapiparser.model.v32.requestBody as requestBody32

class RequestBodySpec: StringSpec ({

    "get request body \$ref" {
        val source = """
            ${'$'}ref: '#/body'
            body: {}
        """

        val body30 = requestBody30(source)
        body30.isRef.shouldBeTrue()
        body30.ref shouldBe "#/body"

        val body31 = requestBody31(source)
        body31.isRef.shouldBeTrue()
        body31.ref shouldBe "#/body"

        val body32 = requestBody32(source)
        body32.isRef.shouldBeTrue()
        body32.ref shouldBe "#/body"
    }

    "get request body from \$ref" {
        val source = """
            ${'$'}ref: '#/requestBody'
                    
            requestBody:
              content:
                application/json:
                  schema:
                    type: object
                    properties:
                        foo: bar
        """

        val body30 = requestBody30(source)
        body30.refObject.content["application/json"].shouldBeInstanceOf<MediaType30>()

        val body31 = requestBody31(source)
        body31.refObject.content["application/json"].shouldBeInstanceOf<MediaType31>()

        val body32 = requestBody32(source)
        body32.refObject.content["application/json"].shouldBeInstanceOf<MediaType32>()
    }

    include(testDescription("requestBody 30", ::requestBody30) { it.description })
    include(testDescription("requestBody 31", ::requestBody31) { it.description })
    include(testDescription("requestBody 32", ::requestBody32) { it.description })

    "gets requestBody content 30" {
        val source = """
            content:
             application/json: {}
             application/xml: {}
        """

        val content = requestBody30(source).content

        content.shouldNotBeNull()
        content.size shouldBe 2
        content["application/json"].shouldBeInstanceOf<MediaType30>()
        content["application/xml"].shouldBeInstanceOf<MediaType30>()
    }

    "gets requestBody content 31" {
        val source = """
            content:
             application/json: {}
             application/xml: {}
        """

        val content = requestBody31(source).content

        content.shouldNotBeNull()
        content.size shouldBe 2
        content["application/json"].shouldBeInstanceOf<MediaType31>()
        content["application/xml"].shouldBeInstanceOf<MediaType31>()
    }

    "gets requestBody content 32" {
        val source = """
            content:
             application/json: {}
             application/xml: {}
        """

        val content = requestBody32(source).content

        content.shouldNotBeNull()
        content.size shouldBe 2
        content["application/json"].shouldBeInstanceOf<MediaType32>()
        content["application/xml"].shouldBeInstanceOf<MediaType32>()
    }

    "gets requestBody content is empty if missing" {
        requestBody30().content.shouldBeEmpty()
        requestBody31().content.shouldBeEmpty()
        requestBody32().content.shouldBeEmpty()
    }

    "gets requestBody required" {
        requestBody30("required: true").required.shouldBeTrue()
        requestBody30("required: false").required.shouldBeFalse()
        requestBody31("required: true").required.shouldBeTrue()
        requestBody31("required: false").required.shouldBeFalse()
        requestBody32("required: true").required.shouldBeTrue()
        requestBody32("required: false").required.shouldBeFalse()
    }

    "gets requestBody required is false if missing" {
        requestBody30().required.shouldBeFalse()
        requestBody31().required.shouldBeFalse()
        requestBody32().required.shouldBeFalse()
    }

    include(testExtensions("requestBody 30", ::requestBody30) { it.extensions })
    include(testExtensions("requestBody 31", ::requestBody31) { it.extensions })
    include(testExtensions("requestBody 32", ::requestBody32) { it.extensions })
})

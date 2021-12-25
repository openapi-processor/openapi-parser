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
import io.openapiparser.model.v30.Response as Response30
import io.openapiparser.model.v30.responses as responses30
import io.openapiparser.model.v31.Response as Response31
import io.openapiparser.model.v31.responses as responses31

class ResponsesSpec: StringSpec({

    "gets responses default" {
        responses30("default: {}").default.shouldNotBeNull()
        responses31("default: {}").default.shouldNotBeNull()
    }

    "gets responses default is null if missing" {
        responses30().default.shouldBeNull()
        responses31().default.shouldBeNull()
    }

    "gets responses responses 30" {
        val source = """
            200: {}
            204: {}
        """

        val responses = responses30(source).responses
        responses.shouldNotBeNull()
        responses.size shouldBe 2
        responses["200"].shouldBeInstanceOf<Response30>()
        responses["204"].shouldBeInstanceOf<Response30>()
    }

    "gets responses responses 31" {
        val source = """
            200: {}
            204: {}
        """

        val responses = responses31(source).responses
        responses.shouldNotBeNull()
        responses.size shouldBe 2
        responses["200"].shouldBeInstanceOf<Response31>()
        responses["204"].shouldBeInstanceOf<Response31>()
    }

    "gets responses responses is empty if missing" {
        responses30().responses.shouldBeEmpty()
        responses31().responses.shouldBeEmpty()
    }

    include(testExtensions("responses 30", ::responses30) { it.extensions })
    include(testExtensions("responses 31", ::responses31) { it.extensions })
})

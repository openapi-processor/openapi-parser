/*
 * Copyright 2025 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.equals.shouldBeEqual
import io.openapiparser.support.ApiBuilder
import io.openapiparser.support.getResponseBody

class SelfRefSpec: StringSpec({

    $$"resolves $self base uri - base uri within content" {
        val api = ApiBuilder()
            .withDocument("https://example.com/api/openapi", "/ref/v32/base-uri-within-content/openapi.yaml")
            .withDocument("https://example.com/api/shared/foo", "/ref/v32/base-uri-within-content/foo.yaml")
            .buildOpenApi32("https://example.com/api/openapi")

        val responseBody = api.getResponseBody("/foo").refObject

        val foo = responseBody.content["application/json"]!!.schema!!.refObject
        foo.type shouldContainExactly listOf("object")
        foo.getRawValue($$"$id")!!.shouldBeEqual("https://example.com/api/schemas/foo")

        val bar = foo.properties["bar"]?.refObject!!
        bar.type shouldContainExactly listOf("string")
        bar.getRawValue($$"$id")!!.shouldBeEqual("https://example.com/api/schemas/bar")
    }

    $$"resolves $self base uri - relative" {
        val api = ApiBuilder()
            .withDocument("https://example.com/api/openapi", "/ref/v32/base-uri-relative/openapi.yaml")
            .withDocument("https://example.com/api/shared/foo", "/ref/v32/base-uri-relative/foo.yaml")
            .buildOpenApi32("https://example.com/api/openapi")

        val responseBody = api.getResponseBody("/foo").refObject

        val foo = responseBody.content["application/json"]!!.schema!!.refObject
        foo.type shouldContainExactly listOf("object")
        foo.getRawValue($$"$id")!!.shouldBeEqual("/api/schemas/foo")

        val bar = foo.properties["bar"]?.refObject!!
        bar.type shouldContainExactly listOf("string")
        bar.getRawValue($$"$id")!!.shouldBeEqual("/api/schemas/bar")
    }

})

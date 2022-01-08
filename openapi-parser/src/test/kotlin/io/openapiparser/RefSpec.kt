/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.openapiparser.support.TestBuilder

class RefSpec: StringSpec({

    "ref-into-another-file" {
        val api = TestBuilder()
            .withResource("/v30/ref-into-another-file/openapi.yaml")
            .buildOpenApi30()

        val pathItem = api.paths.getPathItem("/foo")
        val response = pathItem!!.get!!.responses.getResponse("200")
        val schema = response!!.content["application/json"]!!.schema

        schema!!.ref shouldBe "foo.yaml#/Foo"
        val ref = schema.refObject
        ref.type shouldBe "object"
        ref.properties.size shouldBe 1
        ref.properties["bar"]!!.type shouldBe "string"
    }

})


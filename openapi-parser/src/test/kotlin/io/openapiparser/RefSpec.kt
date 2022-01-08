/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.openapiparser.support.ApiBuilder
import io.openapiparser.support.getResponseSchema

class RefSpec: StringSpec({

    "ref-into-another-file" {
        val api = ApiBuilder()
            .withResource("/v30/ref-into-another-file/openapi.yaml")
            .buildOpenApi30()

        val schema = api.getResponseSchema("/foo", "200", "application/json")

        schema.ref shouldBe "foo.yaml#/Foo"

        val ref = schema.refObject
        ref.type shouldBe "object"
        ref.properties.size shouldBe 1
        ref.properties["bar"]!!.type shouldBe "string"
    }

})


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

    "parses ref into another file" {
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

    "parses ref array items with nested ref" {
        val api = ApiBuilder()
            .withResource("/v30/ref-array-items-nested/openapi.yaml")
            .buildOpenApi30()

        val schema = api.getResponseSchema("/array", "200", "application/json")
        schema.type shouldBe "array"

        val bar = schema.items
        bar!!.ref shouldBe "#/components/schemas/Bar"

        val refBar = bar.refObject
        val foos = refBar.properties["foos"]
        foos!!.type shouldBe "array"

        val foo = foos.items
        foo!!.ref shouldBe "#/components/schemas/Foo"

        val fooRef = foo.refObject
        fooRef.type shouldBe "object"
        fooRef.properties.size shouldBe 1
        fooRef.properties["foo"]!!.type shouldBe "string"
    }

    "parses ref loop" {
        val api = ApiBuilder()
            .withResource("/v30/ref-loop/openapi.yaml")
            .buildOpenApi30()

        val schema = api.getResponseSchema("/self-reference", "200", "application/json")
        schema.ref shouldBe "#/components/schemas/Self"

        val ref = schema.refObject
        ref.type shouldBe "object"
        ref.properties.size shouldBe 1
        ref.properties["self"]!!.ref shouldBe "#/components/schemas/Self"

        val refRef = ref.properties["self"]!!.ref
        ref.type shouldBe "object"
        ref.properties.size shouldBe 1
        ref.properties["self"]!!.ref shouldBe "#/components/schemas/Self"
    }
})


/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.openapiparser.support.ApiBuilder
import io.openapiparser.support.getParameters
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

        val refRef = ref.properties["self"]!!.refObject
        refRef.type shouldBe "object"
        refRef.properties.size shouldBe 1
        refRef.properties["self"]!!.ref shouldBe "#/components/schemas/Self"
    }

    "parses array ref items with nested array and ref items loop" {
        val api = ApiBuilder()
            .withResource("/v30/ref-loop-array/openapi.yaml")
            .buildOpenApi30()

        val schema = api.getResponseSchema("/response-ref", "200", "application/json")

        // response is an array
        schema.type shouldBe "array"

        // with an items $ref to Foo
        val foo = schema.items
        foo!!.ref shouldBe "#/components/schemas/Foo"

        // Foo is an object
        val fooRef = foo.refObject
        fooRef.type shouldBe "object"
        fooRef.properties.size shouldBe 1

        // with a $ref property to Foos
        val foos = fooRef.properties["foos"]
        foos!!.ref shouldBe "#/components/schemas/Foos"

        // Foos is an object
        val foosRef = foos.refObject
        foosRef.type shouldBe "object"
        foosRef.properties.size shouldBe 1

        // with an array property
        val foosArray = foosRef.properties["items"]
        schema.type shouldBe "array"

        // with an items $ref to Foo
        val foosItem = foosArray!!.items
        foosItem!!.ref shouldBe "#/components/schemas/Foo"

        // Foo is the same Foo as above, creating a loop
        val foosItemRef = foosItem.refObject
        foosItemRef.type shouldBe "object"
        foosItemRef.properties.size shouldBe 1
    }

    "parses ref in parameter" {
        val api = ApiBuilder()
            .withResource("/v30/ref-parameter/openapi.yaml")
            .buildOpenApi30()

        val parameters = api.getParameters("/foo")
        parameters.size shouldBe 1

        val parameter = parameters.first()
        parameter.ref shouldBe "#/components/parameters/Bar"

        val parameterRef = parameter.refObject
        parameterRef.`in` shouldBe "query"
        parameterRef.name shouldBe "bar"
    }
})


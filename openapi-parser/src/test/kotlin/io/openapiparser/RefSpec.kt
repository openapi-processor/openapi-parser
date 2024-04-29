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
import io.openapiparser.model.v30.Schema as Schema30

class RefSpec: StringSpec({

    "parses ref into another file" {
        val api = ApiBuilder()
            .buildOpenApi30("/v30/ref-into-another-file/openapi.yaml")

        val schema = api.getResponseSchema("/foo", "200", "application/json")
        schema.ref shouldBe "foo.yaml#/Foo"

        val ref = schema.refObject
        ref.type shouldBe "object"
        ref.properties.size shouldBe 1
        ref.properties["bar"]!!.type shouldBe "string"
    }

    "parses ref array items with nested ref" {
        val api = ApiBuilder()
            .buildOpenApi30("/v30/ref-array-items-nested/openapi.yaml")

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
            .buildOpenApi30("/v30/ref-loop/openapi.yaml")

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
            .buildOpenApi30("/v30/ref-loop-array/openapi.yaml")

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
            .buildOpenApi30("/v30/ref-parameter/openapi.yaml")

        val parameters = api.getParameters("/foo")
        parameters.size shouldBe 1

        val parameter = parameters.first()
        parameter.ref shouldBe "#/components/parameters/Bar"

        val parameterRef = parameter.refObject
        parameterRef.`in` shouldBe "query"
        parameterRef.name shouldBe "bar"
    }

    "parses ref relative to current file" {
        val api = ApiBuilder()
            .buildOpenApi30("/v30/ref-is-relative-to-current-file/openapi.yaml")

        val schema = api.getResponseSchema("/foo", "200", "application/json")
        schema.ref shouldBe "schemas/foo.yaml#/Foo"

        val foo = schema.refObject
        foo.type shouldBe "object"
        foo.properties.size shouldBe 1
        foo.properties["bar"]!!.ref shouldBe "bar.yaml#/Bar"

        val tmp = foo.properties["bar"]
        val bar = tmp!!.refObject
        bar.type shouldBe "object"
        bar.properties.size shouldBe 1
        bar.properties["bar"]!!.type shouldBe "string"
    }

    "parses ref into another file without pointer" {
        val api = ApiBuilder()
            .buildOpenApi30("/v30/ref-into-another-file-path/openapi.yaml")

        val paths = api.paths
        val pathItem = paths.getPathItem("/foo")
        pathItem!!.ref shouldBe "foo.api.yaml"

        val pathItemRef = pathItem.refObject
        val responseSchema = pathItemRef.getValueOf(
            "/get/responses/200/content/application~1json/schema", Schema30::class.java)
        responseSchema!!.ref shouldBe "foo.yaml#/Foo"

        val fooSchema = responseSchema.refObject
        fooSchema.properties["bar"]!!.type shouldBe "string"
    }

    "parses ~ escaped ref path into another file" {
        val api = ApiBuilder()
            .buildOpenApi30("/v30/ref-to-escaped-path-name/openapi.yaml")

        val paths = api.paths

        val pathFoo = paths.getPathItem("/foo")
        pathFoo!!.ref shouldBe "./foo.yaml#/paths/~1foo"

        val refFoo = pathFoo.refObject
        val responseFoo = refFoo.getValueOf(
            "/get/responses/200/content/application~1json/schema", Schema30::class.java)
        responseFoo!!.ref shouldBe "#/components/schemas/Foo"

        val pathFooId = paths.getPathItem("/foo/{id}")
        pathFooId!!.ref shouldBe "./foo.yaml#/paths/~1foo~1{id}"

        val refFooId = pathFooId.refObject
        val responseFooId = refFooId.getValueOf(
            "/get/responses/200/content/application~1json/schema", Schema30::class.java)
        responseFooId!!.ref shouldBe "#/components/schemas/Foo"
    }
})


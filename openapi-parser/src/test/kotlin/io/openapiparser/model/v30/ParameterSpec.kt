/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v30

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.maps.shouldContainKey
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.openapiparser.support.TestBuilder

class ParameterSpec : StringSpec({

    "gets parameter properties" {
        val param = TestBuilder()
            .withYaml("""
                  name: p
                  in: query
                  description: a description
                  required: true
                  deprecated: true
                  allowEmptyValue: true
                  style: simple
                  explode: true
                  allowReserved: true
            """.trimIndent())
            .build(Parameter::class.java)

        param.name shouldBe "p"
        param.`in` shouldBe "query"
        param.description shouldBe "a description"
        param.required shouldBe true
        param.deprecated shouldBe true
        param.allowEmptyValue shouldBe true
        param.style shouldBe "simple"
        param.explode shouldBe true
        param.allowReserved shouldBe true
    }

    "gets schema object" {
        val param = TestBuilder()
            .withApi("""
                schema: {}
            """.trimIndent())
            .build(Parameter::class.java)

        param.schema.shouldNotBeNull()
    }

    "gets example object" {
        val param = TestBuilder()
            .withApi("""
                example: {}
            """.trimIndent())
            .build(Parameter::class.java)

        param.example.shouldNotBeNull()
    }

    "gets example objects" {
        val param = TestBuilder()
            .withApi("""
                examples:
                 foo: {}
                 bar: {}
            """.trimIndent())
            .build(Parameter::class.java)

        param.examples.shouldNotBeNull()
    }

    /*
    "gets content objects" {
        val param = TestBuilder()
            .withApi("""
                content:
                 application/json: {}
            """.trimIndent())
            .build(Parameter::class.java)

        val content = param.content
        content.size shouldBe 1
        content.shouldContainKey("application/json")
    }*/

    "gets parameter with \$ref" {
        val api = TestBuilder()
            .withApi("""
                paths:
                  /foo:
                    parameters:
                      - ${'$'}ref: '#/parameter'
                parameter:
                  name: p
            """.trimIndent())
            .build(OpenApi::class.java)

        val path = api.paths.getPathItem("/foo")
        val param = path?.parameters?.first()
        param?.name shouldBe "p"
    }
})

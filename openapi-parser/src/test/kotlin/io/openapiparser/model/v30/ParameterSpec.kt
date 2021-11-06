/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v30

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.openapiparser.support.TestBuilder

class ParameterSpec : StringSpec({

    "gets parameter object" {
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
            .buildParameter()

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

})

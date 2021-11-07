/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v30

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.openapiparser.support.TestBuilder

class OperationSpec: StringSpec({

    "gets operation properties" {
        val op = TestBuilder()
            .withYaml("""
                tags:
                  - foo
                  - bar
                summary: a summary
                description: a description
                externalDocs: {}
                operationId: op-id
                parameters: []
            """.trimIndent())
            .buildOperation()

        op.tags.shouldContainExactly("foo", "bar")
        op.summary shouldBe "a summary"
        op.description shouldBe "a description"
        op.externalDocs.shouldNotBeNull()
        op.operationId shouldBe "op-id"
        op.parameters.shouldNotBeNull()
    }

})

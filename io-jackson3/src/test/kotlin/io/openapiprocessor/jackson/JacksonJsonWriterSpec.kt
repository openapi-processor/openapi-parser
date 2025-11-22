/*
 * Copyright 2025 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jackson

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.io.StringWriter

class JacksonJsonWriterSpec : StringSpec({

    "writes document to json" {
        val document = mapOf<String, Any>(
            "openapi" to "3.1.0",
            "info" to mapOf(
              "title" to "this is a title",
                "version" to "1.0"),
            "paths" to mapOf(
                "/foo" to mapOf(
                    "get" to mapOf(
                        "responses" to mapOf(
                            "204" to mapOf(
                                "description" to "this is a description"
                            ))))))

        val out = StringWriter()
        val writer = JacksonJsonWriter(out)
        writer.write(document)

        out.toString() shouldBe """
            {
              "openapi" : "3.1.0",
              "info" : {
                "title" : "this is a title",
                "version" : "1.0"
              },
              "paths" : {
                "/foo" : {
                  "get" : {
                    "responses" : {
                      "204" : {
                        "description" : "this is a description"
                      }
                    }
                  }
                }
              }
            }
        """.trimIndent()
           .replace("\n", System.lineSeparator())
    }
})

/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.kotest.core.spec.style.freeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.openapiparser.schema.JsonSchema
import io.openapiparser.schema.JsonSchemaObject
import io.openapiparser.validator.support.Suite
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.name

/**
 * creates test cases from a JSON-Schema-Test-Suite draft folder.
 *
 * include(draftSpec("/suites/JSON-Schema-Test-Suite/tests/draft4"))
 */
fun draftSpec(draftPath: String) = freeSpec {
    val json = ObjectMapper()
    json.registerModule(KotlinModule())

    val draft = Validator::class.java.getResource(draftPath)
    val root = Path.of(draft!!.toURI())

    fun loadSuites(path: Path): Collection<Suite> {
        return json.readValue(
            Files.readAllBytes(path),
            json.typeFactory.constructCollectionType(List::class.java, Suite::class.java))
    }

    @Suppress("UNCHECKED_CAST")
    fun createSchema(schema: Any): JsonSchema {
        return JsonSchemaObject(schema as Map<String, Any>)
    }

    Files.walk(root)
        .filter { path -> !Files.isDirectory(path) }
        .forEach { path ->
            path.name - {
                val suites = loadSuites(path)

                for (suite in suites) {
                    suite.description - {
                        val schema = createSchema(suite.schema)

                        for (test in suite.tests) {
                            test.description {
                                val validator = Validator()
                                val messages = validator.validate(schema, test.data)

                                if (test.valid) {
                                    messages.shouldBeEmpty()
                                } else {
                                    messages.shouldNotBeEmpty()
                                }
                            }
                        }
                    }
                }
            }
        }
}

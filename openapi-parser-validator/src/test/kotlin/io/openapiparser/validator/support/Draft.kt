/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.support

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.kotest.core.spec.style.freeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.openapiparser.converter.Types.asMap
import io.openapiparser.jackson.JacksonConverter
import io.openapiparser.reader.UriReader
import io.openapiparser.schema.*
import io.openapiparser.validator.Validator
import java.net.URI
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.name

/**
 * creates test cases from a JSON-Schema-Test-Suite draft folder.
 *
 * include(draftSpec("/suites/JSON-Schema-Test-Suite/tests/draft4"))
 */
fun draftSpec(draftPath: String, exclude: Array<String> = emptyArray()) = freeSpec {
    val json = ObjectMapper()
    json.registerModule(KotlinModule())

    val draft = Validator::class.java.getResource(draftPath)
    val root = Path.of(draft!!.toURI())

    fun loadSuites(path: Path): Collection<Suite> {
        return json.readValue(
            Files.readAllBytes(path),
            json.typeFactory.constructCollectionType(List::class.java, Suite::class.java))
    }

    fun createSchema(schema: Any): JsonSchema {
        val resolver = Resolver(UriReader(), JacksonConverter(), DocumentStore())
        val result = resolver.resolve(URI.create(""), schema)
        return JsonSchemaObject(asMap(schema), JsonSchemaContext(result.uri, result.registry))
    }

    fun createInstance(instance: Any?): JsonInstance {
        val resolver = Resolver(UriReader(), JacksonConverter(), DocumentStore())
        val result = resolver.resolve(URI.create(""), instance)
        return JsonInstance(instance, JsonInstanceContext(result.uri, result.registry))
    }

    Files.walk(root)
        .filter { path -> !Files.isDirectory(path) }
        .filter { path -> !exclude.contains(path.name) }
        .forEach { path ->
            path.name - {
                val suites = loadSuites(path)

                for (suite in suites) {
                    suite.description - {
                        val schema = createSchema(suite.schema)

                        val tests = suite.tests
                            .filter { t -> !exclude.contains(t.description) }

                        for (test in tests) {
                            test.description {
                                val instance = createInstance(test.data)

                                val validator = Validator()
                                val messages = validator.validate(schema, instance)

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

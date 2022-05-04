/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.support

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.kotest.core.spec.style.freeSpec
import io.kotest.matchers.shouldBe
import io.openapiparser.converter.Types.asMap
import io.openapiparser.jackson.JacksonConverter
import io.openapiparser.schema.*
import io.openapiparser.validator.Validator
import io.openapiparser.validator.ValidatorSettings
import java.net.URI
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.name

/**
 * creates test cases from a JSON-Schema-Test-Suite draft folder.
 *
 * include(draftSpec("/suites/JSON-Schema-Test-Suite/tests/draft4"))
 */
fun draftSpec(
    draftPath: String,
    settings: ValidatorSettings = ValidatorSettings(),
    extras: List<Any> = emptyList()) = freeSpec {

    val json = ObjectMapper()
    json.registerModule(KotlinModule())

    val draft = Validator::class.java.getResource(draftPath)
    val root = Path.of(draft!!.toURI())

    val excludes = extras
        .filterIsInstance(Exclude::class.java)
        .map { e -> e.test }

    val remotes = extras
        .filterIsInstance(Remote::class.java)

    fun toPath(path: String): Path {
        val resource = Validator::class.java.getResource(path)
        return Path.of(resource!!.toURI())
    }

    fun loadSuites(path: Path): Collection<Suite> {
        return json.readValue(
            Files.readAllBytes(path),
            json.typeFactory.constructCollectionType(List::class.java, Suite::class.java))
    }

    fun loadDocument(path: String): Any {
        return json.readValue(
            Files.readAllBytes(toPath(path)),
            json.typeFactory.constructMapType(HashMap::class.java, String::class.java, Object::class.java)
        )
    }

    fun createSchema(schema: Any, documents: List<Document>): JsonSchema {
        val resolver = Resolver(
            TestUriReader(documents),
            JacksonConverter(),
            DocumentStore(),
            settings.version
        )
        val result = resolver.resolve(URI.create(""), schema)

        return if (schema is Boolean) {
            JsonSchemaBoolean(
                schema,
                JsonSchemaContext(result.uri, result.registry, settings.version))
        } else {
            JsonSchemaObject(
                asMap(schema)!!,
                JsonSchemaContext(result.uri, result.registry, settings.version))
        }
    }

    fun createInstance(instance: Any?): JsonInstance {
        return JsonInstance(instance, JsonInstanceContext(URI.create(""), ReferenceRegistry()))
    }

    Files.walk(root)
        .filter { path -> !Files.isDirectory(path) }
        .filter { path -> !excludes.contains(path.name) }
        .forEach { path ->
            path.name - {
                val suites = loadSuites(path)

                for (suite in suites) {
                    val documents = remotes
                        .filter { e -> e.test == suite.description }
                        .map { e -> e.document }

                    suite.description - {
                        val schema = createSchema(suite.schema, documents)

                        val tests = suite.tests
                            .filter { t -> !excludes.contains(t.description) }

                        for (test in tests) {
                            test.description {
                                val instance = createInstance(test.data)

                                // act
                                val validator = Validator(settings)
                                val step = validator.validate(schema, instance)

                                // assert
                                step.isValid.shouldBe(test.valid)
                            }
                        }
                    }
                }
            }
        }
}

class Exclude(val test: String)
class Remote(val test: String, val document: Document)
class Document(val id: String, val path: String)

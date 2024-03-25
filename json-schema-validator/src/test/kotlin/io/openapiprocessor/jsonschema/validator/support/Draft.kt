/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.support

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.kotest.core.spec.style.freeSpec
import io.kotest.matchers.shouldBe
import io.openapiprocessor.jackson.JacksonConverter
import io.openapiprocessor.jsonschema.ouput.OutputConverter
import io.openapiprocessor.jsonschema.schema.*
import io.openapiprocessor.jsonschema.support.Uris.createUri
import io.openapiprocessor.jsonschema.validator.Validator
import io.openapiprocessor.jsonschema.validator.ValidatorSettings
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
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
    json.registerModule(KotlinModule.Builder().build())

    val draft = Validator::class.java.getResource(draftPath)
    val root = Paths.get(draft!!.toURI())

    val excludes = extras
        .filterIsInstance<Exclude>()
        .map { e -> e.test }

    val settingsModifier = extras
        .filterIsInstance<Settings>()
        .map { s -> s.test to s.modify }
        .toMap()

    fun loadSuites(path: Path): Collection<Suite> {
        return json.readValue(
            Files.readAllBytes(path),
            json.typeFactory.constructCollectionType(
                List::class.java,
                Suite::class.java
            )
        )
    }

    fun createStore(loader: DocumentLoader): SchemaStore {
        val store = SchemaStore(loader)

        when (settings.version) {
            SchemaVersion.Draft202012 -> store.registerDraft202012()
            SchemaVersion.Draft201909 -> {
                store.registerDraft7()
                store.registerDraft201909()
                store.registerDraft202012()
            }
            SchemaVersion.Draft7 -> {
                store.registerDraft7()
                store.registerDraft201909()
            }
            SchemaVersion.Draft6 -> store.registerDraft6()
            SchemaVersion.Draft4 -> store.registerDraft4()
        }

        return store
    }

    fun createSchema(schema: Any): JsonSchema {
        val loader = DocumentLoader(
            TestRemotesUriReader(),
            JacksonConverter()
        )
        val store = createStore(loader)
//        val uri = createUri("urn:uuid:${UUID(0L, 0L)})")
//        val uri = createUri("urn:test:0")
        val uri = createUri("https://test/")
        store.register(uri, schema)
        return store.getSchema(uri, settings.version)
    }

    fun createInstance(instance: Any?): JsonInstance {
        return JsonInstance(JsonPointer.empty(), instance)
    }

    fun createTestName(testPath: Path): String {
        val path = testPath.toAbsolutePath().toString()
        val draftPathIndex = path.indexOf(draftPath)
        return path.substring(draftPathIndex + draftPath.length + 1)
    }

    Files.walk(root)
        .filter { path -> !Files.isDirectory(path) }
        .filter { path -> !excludes.contains(path.name) }
        .forEach { path ->
            createTestName(path) - {
                val suites = loadSuites(path)

                for (suite in suites) {
                    suite.description - {
                        val schema = createSchema(suite.schema)

                        val tests = suite.tests
                            .filter { t -> !excludes.contains(t.description) }

                        for (test in tests) {
                            test.description {
                                var currentSettings = ValidatorSettings(settings)
                                val modifier = settingsModifier[test.description]
                                if(modifier != null) {
                                    currentSettings = modifier(currentSettings)
                                }

                                val instance = createInstance(test.data)

                                // act
                                val validator = Validator(currentSettings)
                                val step = validator.validate(schema, instance)

                                // output
                                val converter = OutputConverter(Output.VERBOSE)
                                val output = converter.convert(step)

                                val mapper = ObjectMapper()
                                mapper.enable(SerializationFeature.INDENT_OUTPUT)
                                mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
                                println(mapper.writeValueAsString(output))

                                // check valid
                                step.isValid.shouldBe(test.valid)
                            }
                        }
                    }
                }
            }
        }
}

class Exclude(val test: String)
class Settings(val test: String, val modify: (ValidatorSettings) -> ValidatorSettings)
class Document(val id: String, val path: String)

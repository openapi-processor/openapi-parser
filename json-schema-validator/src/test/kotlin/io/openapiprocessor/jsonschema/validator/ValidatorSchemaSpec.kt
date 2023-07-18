/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.openapiprocessor.jsonschema.reader.UriReader
import io.openapiprocessor.jsonschema.schema.DocumentLoader
import io.openapiprocessor.jsonschema.schema.JsonInstance
import io.openapiprocessor.jsonschema.schema.SchemaStore
import io.openapiprocessor.jsonschema.schema.SchemaVersion
import io.openapiprocessor.snakeyaml.SnakeYamlConverter
import io.openapiprocessor.jsonschema.validator.support.InstanceBuilder


class ValidatorSchemaSpec : StringSpec({

    val loader = DocumentLoader(
        UriReader(),
        SnakeYamlConverter()
    )
    val store = SchemaStore(loader)
    store.registerDraft4()
    store.registerDraft6()
    store.registerDraft7()
    store.registerDraft201909()
    store.registerDraft202012()

    fun getInstance (schema: SchemaVersion): JsonInstance {
        return InstanceBuilder(loader, store.documents).getDraft(schema)
    }

    data class Fixture(val schema: SchemaVersion, val instance: SchemaVersion)

    val fixtures = listOf(
        Fixture(SchemaVersion.Draft4, SchemaVersion.Draft4),
        Fixture(SchemaVersion.Draft6, SchemaVersion.Draft6),
        Fixture(SchemaVersion.Draft7, SchemaVersion.Draft7),
        Fixture(SchemaVersion.Draft201909, SchemaVersion.Draft201909),
        Fixture(SchemaVersion.Draft202012, SchemaVersion.Draft202012)
    )

    for (f in fixtures) {
        "validate ${f.instance.name} schema with ${f.schema.name}" {
            val schema = store.getSchema(f.schema.schemaUri, f.schema)
            val instance = getInstance(f.instance)

            val validator = Validator()
            val step = validator.validate(schema, instance)

            step.isValid.shouldBeTrue()
        }
    }
})

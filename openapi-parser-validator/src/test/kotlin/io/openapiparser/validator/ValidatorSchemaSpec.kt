/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.openapiparser.reader.UriReader
import io.openapiparser.schema.DocumentLoader
import io.openapiparser.schema.JsonInstance
import io.openapiparser.schema.SchemaStore
import io.openapiparser.schema.SchemaVersion
import io.openapiparser.snakeyaml.SnakeYamlConverter
import io.openapiparser.validator.support.InstanceBuilder


class ValidatorSchemaSpec : StringSpec({

    val loader = DocumentLoader(UriReader(), SnakeYamlConverter())
    val store = SchemaStore(loader)
    store.registerDraft4()
    store.registerDraft6()
    store.registerDraft7()
    store.registerDraft201909()

    fun getInstance (schema: SchemaVersion): JsonInstance {
        return InstanceBuilder(loader, store.documents).getDraft(schema)
    }

    data class Fixture(val schema: SchemaVersion, val instance: SchemaVersion)

    val fixtures = listOf(
        Fixture(SchemaVersion.Draft4, SchemaVersion.Draft4),
        Fixture(SchemaVersion.Draft6, SchemaVersion.Draft6),
        Fixture(SchemaVersion.Draft7, SchemaVersion.Draft7),
        Fixture(SchemaVersion.Draft201909, SchemaVersion.Draft201909)
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

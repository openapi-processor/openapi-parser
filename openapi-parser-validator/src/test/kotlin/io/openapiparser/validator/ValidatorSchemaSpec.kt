/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.openapiparser.reader.UriReader
import io.openapiparser.schema.*
import io.openapiparser.snakeyaml.SnakeYamlConverter

class ValidatorSchemaSpec : StringSpec({

    "validates draft 4 with draft 4" {
        val resolver = Resolver(UriReader(), SnakeYamlConverter(), DocumentStore())
        val store = SchemaStore(resolver)
        store.loadDraft4()

        val schema = store.getSchema(SchemaVersion.Draft4.schema)

        val resolverResult = resolver.resolve("/json-schema/draft-04/schema.json")
        val instanceContext = JsonInstanceContext(resolverResult.uri, ReferenceRegistry())
        val instance = JsonInstance(resolverResult.document, instanceContext)

        val validator = Validator()
        val step = validator.validate(schema!!, instance)

        step.isValid.shouldBeTrue()
    }

})

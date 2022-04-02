/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.openapiparser.reader.UriReader
import io.openapiparser.schema.*
import io.openapiparser.snakeyaml.SnakeYamlConverter

class ValidatorSchemaSpec : StringSpec({

    "validates draft4 & refs with draft4" {
        val resolver = Resolver(UriReader(), SnakeYamlConverter(), DocumentStore())
        val store = SchemaStore(resolver)
        store.loadDraft4()

        val schema = store.getSchema(SchemaVersions.DRAFT4)

        val resolverResult = resolver.resolve("/json-schema/draft-04/schema.json")
        val instanceContext = JsonInstanceContext(resolverResult.uri, resolverResult.registry)
        val instance = JsonInstance(resolverResult.document, instanceContext)

        val validator = Validator()
        val messages = validator.validate(schema, instance)

        messages.shouldBeEmpty()
    }

})

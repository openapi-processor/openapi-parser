/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.support

import io.openapiprocessor.jsonschema.schema.*

class InstanceBuilder(private val loader: DocumentLoader, private val documents: DocumentStore) {
    fun getDraft(version: SchemaVersion): JsonInstance {
        val document = documents.get(version.schemaUri)!!
        val resolver = Resolver(
            documents,
            loader,
            Resolver.Settings(version)
        )
        val resolverResult = resolver.resolve(version.schemaUri, document)
        return JsonInstance(resolverResult.document)
    }
}

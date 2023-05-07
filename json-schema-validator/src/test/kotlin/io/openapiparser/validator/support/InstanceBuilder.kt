/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.support

import io.openapiparser.schema.*

class InstanceBuilder(private val loader: DocumentLoader, private val documents: DocumentStore) {
    fun getDraft(version: SchemaVersion): JsonInstance {
        val document = documents.get(version.schemaUri)!!
        val resolver = Resolver (documents, loader, Resolver.Settings (version))
        val resolverResult = resolver.resolve(version.schemaUri, document)
        val instanceContext = JsonInstanceContext(resolverResult.scope, ReferenceRegistry())
        return JsonInstance(resolverResult.document, instanceContext)
    }
}

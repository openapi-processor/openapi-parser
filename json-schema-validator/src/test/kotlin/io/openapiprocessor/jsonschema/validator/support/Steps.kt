/*
 * Copyright 2023 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.support

import io.openapiprocessor.jsonschema.schema.*
import io.openapiprocessor.jsonschema.schema.Scope.createScope
import io.openapiprocessor.jsonschema.schema.UriSupport.createUri

fun createSchema(document: Map<String, Any>, location: JsonPointer, documentUri: String): JsonSchema {
    val scope = createScope(createUri(documentUri), document, SchemaVersion.Draft201909)
    val context = JsonSchemaContext(
        scope,
        ReferenceRegistry(),
        Vocabularies.ALL
    )
    return JsonSchemaObject(location, document, context)
}

fun createInstance(document: Map<String, Any>, location: JsonPointer): JsonInstance {
    val scope = createScope(createUri(""), document, SchemaVersion.Draft201909)
    val context = JsonInstanceContext(
        scope,
        ReferenceRegistry()
    )
    return JsonInstance(location, document, context)
}

/*
 * Copyright 2025 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v30

import io.openapiparser.Keywords.OPENAPI
import io.openapiparser.OpenApiSchemas.OPENAPI_VERSION_30_LATEST

@Suppress("UNCHECKED_CAST")
fun OpenApi.setVersion() {
    val properties = getRawValueOf("") as MutableMap<String, Any?>
    properties[OPENAPI] = OPENAPI_VERSION_30_LATEST
}

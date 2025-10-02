/*
 * Copyright 2025 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.support

import io.kotest.matchers.nulls.shouldNotBeNull
import io.openapiparser.model.v31.OpenApi as OpenApi31
import io.openapiparser.model.v31.Parameter as Parameter31
import io.openapiparser.model.v31.Responses as Responses31
import io.openapiparser.model.v31.Schema as Schema31

fun OpenApi31.getResponseSchema(path: String, status: String, media: String): Schema31 {
    val pathItem = paths?.getPathItem(path)
    pathItem.shouldNotBeNull()

    val operation = pathItem.get
    operation.shouldNotBeNull()

    val response = operation.responses?.getResponse(status)
    response.shouldNotBeNull()

    val content = response.content[media]
    content.shouldNotBeNull()

    val schema = content.schema
    schema.shouldNotBeNull()

    return schema
}

fun OpenApi31.getParameters(path: String): Collection<Parameter31> {
    val pathItem = paths?.getPathItem(path)
    pathItem.shouldNotBeNull()

    val operation = pathItem.get
    operation.shouldNotBeNull()

    return operation.parameters
}

fun OpenApi31.getResponses(path: String): Responses31? {
    val pathItem = paths?.getPathItem(path)
    pathItem.shouldNotBeNull()

    val operation = pathItem.get
    operation.shouldNotBeNull()

    return operation.responses
}


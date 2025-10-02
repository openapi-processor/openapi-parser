/*
 * Copyright 2025 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.support

import io.kotest.matchers.nulls.shouldNotBeNull
import io.openapiparser.model.v32.RequestBody as RequestBody32
import io.openapiparser.model.v32.OpenApi as OpenApi32
import io.openapiparser.model.v32.Parameter as Parameter32
import io.openapiparser.model.v32.Responses as Responses32
import io.openapiparser.model.v32.Schema as Schema32

fun OpenApi32.getResponseBody(path: String): RequestBody32 {
    val pathItem = paths?.getPathItem(path)
    pathItem.shouldNotBeNull()

    val operation = pathItem.get
    operation.shouldNotBeNull()

    val body = operation.requestBody
    body.shouldNotBeNull()

    return body
}

fun OpenApi32.getResponseSchema(path: String, status: String, media: String): Schema32 {
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

fun OpenApi32.getParameters(path: String): Collection<Parameter32> {
    val pathItem = paths?.getPathItem(path)
    pathItem.shouldNotBeNull()

    val operation = pathItem.get
    operation.shouldNotBeNull()

    return operation.parameters
}

fun OpenApi32.getResponses(path: String): Responses32? {
    val pathItem = paths?.getPathItem(path)
    pathItem.shouldNotBeNull()

    val operation = pathItem.get
    operation.shouldNotBeNull()

    return operation.responses
}

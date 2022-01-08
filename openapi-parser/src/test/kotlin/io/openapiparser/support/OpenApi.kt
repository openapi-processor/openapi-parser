/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.support

import io.kotest.matchers.nulls.shouldNotBeNull
import io.openapiparser.model.v30.OpenApi as OpenApi30
import io.openapiparser.model.v30.Parameter as Parameter30
import io.openapiparser.model.v30.Schema as Schema30

fun OpenApi30.getResponseSchema(path: String, status: String, media: String): Schema30 {
    val pathItem = paths.getPathItem(path)
    pathItem.shouldNotBeNull()

    val operation = pathItem.get
    operation.shouldNotBeNull()

    val response = operation.responses.getResponse(status)
    response.shouldNotBeNull()

    val content = response.content[media]
    content.shouldNotBeNull()

    val schema = content.schema
    schema.shouldNotBeNull()

    return schema
}

fun OpenApi30.getParameters(path: String): Collection<Parameter30> {
    val pathItem = paths.getPathItem(path)
    pathItem.shouldNotBeNull()

    val operation = pathItem.get
    operation.shouldNotBeNull()

    return operation.parameters
}


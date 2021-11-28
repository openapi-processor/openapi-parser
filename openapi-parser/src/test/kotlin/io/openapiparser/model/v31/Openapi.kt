/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v31

import io.openapiparser.support.buildObject

fun openapi(content: String = "{}"): OpenApi {
    return buildObject(OpenApi::class.java, content)
}

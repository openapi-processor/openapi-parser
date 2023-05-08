/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jackson.support

import io.openapiprocessor.jackson.JacksonConverter

@Suppress("UNCHECKED_CAST")
fun JacksonConverter.convertToMap(api: String): Map<String, Any> {
    return this.convert(api) as Map<String, Any>
}

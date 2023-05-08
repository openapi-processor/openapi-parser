/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.snakeyaml.support

import io.openapiprocessor.snakeyaml.SnakeYamlConverter

@Suppress("UNCHECKED_CAST")
fun SnakeYamlConverter.convertToMap(api: String): Map<String, Any> {
    return this.convert(api) as Map<String, Any>
}

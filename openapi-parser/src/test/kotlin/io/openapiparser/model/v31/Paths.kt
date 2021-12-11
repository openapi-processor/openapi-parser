/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v31

import io.openapiparser.support.buildObject

fun paths(content: String = "{}"): Paths {
    return buildObject(Paths::class.java, content)
}

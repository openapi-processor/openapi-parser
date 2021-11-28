/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v30

import io.openapiparser.support.buildObject

fun license(content: String = "{}"): License {
    return buildObject(License::class.java, content)
}

/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v30

import io.openapiparser.support.buildObject

fun contact(content: String = "{}"): Contact {
    return buildObject(Contact::class.java, content)
}
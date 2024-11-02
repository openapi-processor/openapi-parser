/*
 * Copyright 2024 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.ov10

import io.openapiparser.support.buildObject

fun overlay(content: String = "{}"): Overlay {
    return buildObject(Overlay::class.java, content)
}

fun info(content: String = "{}"): Info {
    return buildObject(Info::class.java, content)
}

fun action(content: String = "{}"): Action {
    return buildObject(Action::class.java, content)
}

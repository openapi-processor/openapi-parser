/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.support

import io.openapiparser.validator.messages.ValidationMessage

@Deprecated("obsolete")
fun ValidationMessage.matches(path: String, text: String): Boolean {
    return this.path == path && this.text.contains(text)
}

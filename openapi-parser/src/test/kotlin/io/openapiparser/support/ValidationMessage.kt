package io.openapiparser.support

import io.openapiparser.ValidationMessage

fun ValidationMessage.matches(path: String, text: String): Boolean {
    return this.path == path && this.text.contains(text)
}

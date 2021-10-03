package io.openapiparser.support

import io.openapiparser.model.v30.Server as Server30

fun Server30.matches(url: String, description: String): Boolean {
    return this.url == url
        && this.description == description
}

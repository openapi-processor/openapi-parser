package io.openapiparser.support

import io.openapiparser.model.v30.ServerVariable as ServerVariable30

fun ServerVariable30.matches(default: String, description: String): Boolean {
    return this.default == default
        && this.description == description
}

fun ServerVariable30.matches(default: String, description: String, enum: List<String>): Boolean {
    return this.default == default
        && this.description == description
        && this.enum.containsAll(enum)
        && this.enum.size == enum.size
}

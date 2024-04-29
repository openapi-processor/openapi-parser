/*
 * Copyright 2024 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.support

import io.openapiprocessor.interfaces.Reader
import io.openapiprocessor.jsonschema.reader.UriReader
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.net.URI
import java.nio.charset.StandardCharsets

class TestReader: Reader {
    private val uriReader = UriReader()

    private val apis = mutableMapOf<URI, String>()
    private val resources = mutableSetOf<URI>()

    override fun read(uri: URI): InputStream {
        if (apis.contains(uri)) {
            return ByteArrayInputStream(apis[uri]?.toByteArray(StandardCharsets.UTF_8))
        }

        return uriReader.read(uri)
    }

    fun addApi(uri: URI, content: String) {
        apis[uri] = content
    }
}

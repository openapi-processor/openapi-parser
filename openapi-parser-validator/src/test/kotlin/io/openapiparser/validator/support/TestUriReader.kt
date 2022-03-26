/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.support

import io.openapiparser.reader.UriReader
import java.io.InputStream
import java.net.URI

class TestUriReader(private val documents: List<Document>) : UriReader() {

    override fun read(uri: URI): InputStream {
        var source = uri

        val first = documents.firstOrNull {
            it.id == uri.toString()
        }

        if (first != null) {
            val resource = TestUriReader::class.java.getResource(first.path)
            source = resource!!.toURI()
        }

        return super.read(source)
    }
}

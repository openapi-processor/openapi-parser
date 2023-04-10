/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.support

import io.openapiparser.reader.UriReader
import org.slf4j.Logger
import org.slf4j.LoggerFactory
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

class TestRemotesUriReader(): UriReader() {
    private var log: Logger = LoggerFactory.getLogger(this.javaClass.name)

    override fun read(uri: URI): InputStream {
        var source = uri;

        val localhostUri = uri.toString();
        if (localhostUri.startsWith("http://localhost:1234")) {
            val resourcePath = localhostUri.replace(
                "http://localhost:1234",
                "/suites/JSON-Schema-Test-Suite/remotes")

            val resource = TestRemotesUriReader::class.java.getResource(resourcePath)
            source = resource!!.toURI()
        }

        val remoteUri = source.toString();
        if (remoteUri.startsWith("http://") || remoteUri.startsWith("https://")) {
            log.warn("requesting remote document: {}", remoteUri);
        }

        return super.read(source)
    }
}

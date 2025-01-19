/*
 * Copyright 2025 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.support

import io.openapiparser.OverlayParser
import io.openapiparser.OverlayResult
import io.openapiprocessor.interfaces.Converter
import io.openapiprocessor.jsonschema.schema.DocumentLoader
import io.openapiprocessor.jsonschema.schema.DocumentStore
import io.openapiprocessor.snakeyaml.SnakeYamlConverter
import java.net.URI

class OverlayBuilder {
    private val reader = TestReader()
    private var converter: Converter = SnakeYamlConverter()
    private var documents: DocumentStore = DocumentStore()

    fun withOverlay(apiYaml: String): OverlayBuilder {
        reader.addApi(URI("file:///overlay.yaml"), apiYaml.trimIndent())
        return this
    }

    fun buildOverlay10(): OverlayResult {
        return buildParser().parse(URI("file:///overlay.yaml"))
    }

    fun buildParser(): OverlayParser {
        return OverlayParser(documents, DocumentLoader(reader, converter))
    }
}

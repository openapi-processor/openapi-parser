/*
 * Copyright 2025 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.openapiparser.model.v31.OpenApi
import io.openapiprocessor.jsonschema.reader.UriReader
import io.openapiprocessor.jsonschema.schema.DocumentLoader
import io.openapiprocessor.jsonschema.schema.DocumentStore
import io.openapiprocessor.snakeyaml.SnakeYamlConverter
import java.net.URI

class BundleSpec : StringSpec({
    val loader = DocumentLoader(UriReader(), SnakeYamlConverter())

    fun parse(documentUri: String): OpenApiResult {
        val documents = DocumentStore()
        val parser = OpenApiParser(documents, loader)
        return parser.parse(documentUri)
    }

    fun parse(documentUri: String, document: Any): OpenApiResult {
        val documents = DocumentStore()
        val parser = OpenApiParser(documents, loader)
        return parser.parse(URI.create(documentUri), document)
    }

    "parse bundled openapi" {
        val result = parse("/bundle-ref-nested/openapi31.yaml")
        val bundled = parse("/bundle-ref-nested/openapi31.yaml", result.bundle())

        val api = bundled.getModel(OpenApi::class.java)
        api.components!!.schemas.size shouldBe 2
    }
})

/*
 * Copyright 2025 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.openapiparser.model.v31.OpenApi
import io.openapiprocessor.jackson.JacksonYamlWriter
import io.openapiprocessor.jsonschema.reader.UriReader
import io.openapiprocessor.jsonschema.schema.DocumentLoader
import io.openapiprocessor.jsonschema.schema.DocumentStore
import io.openapiprocessor.snakeyaml.SnakeYamlConverter
import java.io.StringWriter
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

    "bundle & save example" {
        // val loader = DocumentLoader(UriReader(), SnakeYamlConverter())

        // parse a multi file OpeAPI document (from resources)
        val originalDocs = DocumentStore()
        val originalParser = OpenApiParser(originalDocs, loader)
        val originalResult = originalParser.parse("/bundle-ref-nested/openapi31.yaml")

        // bundle the OpenAPI, i.e. move $ref content to "components.schemas" and friends if the
        // ref points to a different file. The original document is not touched.
        val bundled = originalResult.bundle()

        // parse the bundled document (this creates a reference map for ref lookups) to navigate
        // its OpenAPI model.
        val bundledDocs = DocumentStore()
        val bundledParser = OpenApiParser(bundledDocs, loader)
        val bundledResult = bundledParser.parse(URI.create("/bundle-ref-nested/openapi31.yaml"), bundled)

        // We could use the same document store, but it would override the original (root) document
        // if we use the same id (in this case file name). We can use a different id to avoid that.
        //val bundledParser = OpenApiParser(bundledDocs, loader)
        //val bundledResult = bundledParser.parse(URI.create("/bundled/openapi.yaml"), bundled)

        val api = bundledResult.getModel(OpenApi::class.java)
        api.info.summary

        // write the bundled OpenAPI document. Use the alternative constructor to configure the
        // jackson, snake yaml specific formatting.
        val out = StringWriter()
//        val out = FileWriter("bundled.yaml")
        val writer = JacksonYamlWriter(out)
        //val writer = SnakeYamlWriter(out)
        //val writer = JacksonJsonWriter(out)
        bundledResult.write(writer)

        println(out.toString())
    }
})

/*
 * Copyright 2025 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldNotMatchEach
import io.kotest.matchers.equals.shouldNotBeEqual
import io.kotest.matchers.maps.shouldNotHaveKey
import io.openapiparser.model.v31.OpenApi
import io.openapiprocessor.jsonschema.reader.UriReader
import io.openapiprocessor.jsonschema.schema.DocumentLoader
import io.openapiprocessor.jsonschema.schema.DocumentStore
import io.openapiprocessor.snakeyaml.SnakeYamlConverter

class OverlayApplySpec: StringSpec({

    "remove from object" {
        val documents = DocumentStore()
        val loader = DocumentLoader(UriReader(), SnakeYamlConverter())

        val openApiParser = OpenApiParser(documents, loader)
        val openApiResult = openApiParser.parse("/overlay-remove/openapi.yaml")

        val overlayParser = OverlayParser(documents, loader)
        val overlayResult = overlayParser.parse("/overlay-remove/overlay.yaml")

        openApiResult.apply(overlayResult)

        val api = openApiResult.getModel(OpenApi::class.java)

        val schema = api.components!!.schemas["Foo"]
        schema!!.properties shouldNotHaveKey "foo"
    }

    "remove from array" {
        val documents = DocumentStore()
        val loader = DocumentLoader(UriReader(), SnakeYamlConverter())

        val openApiParser = OpenApiParser(documents, loader)
        val openApiResult = openApiParser.parse("/overlay-remove/openapi.yaml")

        val overlayParser = OverlayParser(documents, loader)
        val overlayResult = overlayParser.parse("/overlay-remove/overlay.yaml")

        openApiResult.apply(overlayResult)

        val api = openApiResult.getModel(OpenApi::class.java)

        val parameters = api.paths!!.getPathItem("/foo")!!.parameters
        parameters.shouldNotMatchEach({ it.name shouldNotBeEqual "p1" })
    }

})

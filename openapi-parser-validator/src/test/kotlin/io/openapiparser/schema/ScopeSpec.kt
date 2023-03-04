/*
 * Copyright 2023 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.net.URI

class ScopeSpec : StringSpec({

    "base uri is document uri if base uri is empty" {
        val documentUri = URI.create("document")
        val baseUri = null
        val scope = Scope(documentUri, baseUri, SchemaVersion.Draft4)

        scope.baseUri shouldBe documentUri
    }

    "base uri is base uri if base uri is not empty" {
        val documentUri = URI.create("document")
        val baseUri = URI.create("base")
        val scope = Scope(documentUri, baseUri, SchemaVersion.Draft4)

        scope.baseUri shouldBe baseUri
    }

    "create scope without id" {
        val documentUri = URI.create("document")
        val document = mapOf<String, Any?>(
            "no" to "id"
        )

        val scope = Scope.createScope(documentUri, document, SchemaVersion.Draft4)

        scope.baseUri shouldBe documentUri
    }

    "create scope with id" {
        val documentUri = URI.create("document")
        val document = mapOf<String, Any?>(
            "id" to "id"
        )

        val scope = Scope.createScope(documentUri, document, SchemaVersion.Draft4)

        scope.baseUri shouldBe URI.create("id")
    }
})

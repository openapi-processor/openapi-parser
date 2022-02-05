/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.net.URI

class ReferenceRegistrySpec : StringSpec({

    "resolve reference" {
        val registry = ReferenceRegistry()
        val document = mapOf(
            "${'$'}ref" to "#/definitions/foo",
            "definitions" to mapOf<String, Any>("foo" to emptyMap<String, Any>())
        )

        val documentUri = URI.create("https://document")
        registry.add(documentUri, documentUri, "#/definitions/foo")

        registry.resolve { uri, ref ->
            uri shouldBe documentUri
            ref shouldBe "#/definitions/foo"
            return@resolve (document["definitions"] as Map<String, Any>)["foo"]
        }

        val ref = registry.getRef(URI.create("https://document#/definitions/foo"))
        ref.rawValue shouldBe emptyMap<String, Any>()
    }

})

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
        val document = mapOf(
            "${'$'}ref" to "#/definitions/foo",
            "definitions" to mapOf<String, Any>("foo" to emptyMap<String, Any>())
        )

        val registry = ReferenceRegistry()
        registry.add(Ref(URI("https://document"), "#/definitions/foo"))

        registry.resolve {
            return@resolve RawValue(it.scope, (document["definitions"] as Map<*, *>)["foo"])
        }

        val ref = registry.getRef(URI("https://document#/definitions/foo"))
        ref.documentUri shouldBe URI("https://document")
        ref.rawValue shouldBe emptyMap<String, Any>()
    }

})

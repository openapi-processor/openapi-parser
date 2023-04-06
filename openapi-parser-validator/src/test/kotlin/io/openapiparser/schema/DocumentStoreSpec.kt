/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import java.net.URI

class DocumentStoreSpec : StringSpec({

    "stores document" {
        val ds = DocumentStore()
        val uri = URI.create("https://any")

        val document = emptyMap<String, Any>()
        ds.addId(uri, document)

        ds.contains(uri).shouldBeTrue()
        ds.get(uri).shouldBe(document)
    }

})

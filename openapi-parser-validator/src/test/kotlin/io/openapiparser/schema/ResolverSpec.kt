/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.maps.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.openapiparser.jackson.JacksonConverter
import io.openapiparser.reader.StringReader
import java.net.URI

class ResolverSpec: StringSpec({

    "resolves schema" {
        val store = DocumentStore()
        val resolver = Resolver(StringReader(""), JacksonConverter(), store)

        val uri = URI.create("https://document")
        val doc = emptyMap<String, Any>()
        val result = resolver.resolve(uri, doc)

        result.uri shouldBe uri
        result.document shouldBe doc
        result.registry.shouldNotBeNull()
    }

    "loads & resolves schema" {
        val store = DocumentStore()
        val content = """
            ${'$'}ref: '#/definitions/foo'
            definitions:
              foo: {}
        """.trimIndent()
        val resolver = Resolver(StringReader(content), JacksonConverter(), store)

        val uri = URI.create("https://document")
        val result = resolver.resolve(uri)

        result.uri shouldBe uri
        val doc = result.document as Map<String, Any>
        doc.shouldHaveSize(2)
        val ref = result.registry.getRef(URI.create("https://document#/definitions/foo"))
        ref.rawValue.shouldNotBeNull()
    }

})

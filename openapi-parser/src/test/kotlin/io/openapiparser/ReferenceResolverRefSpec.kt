/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser

import io.kotest.core.spec.style.StringSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import io.openapiparser.jackson.JacksonConverter
import io.openapiparser.memory.Memory
import io.openapiparser.reader.UriReader
import java.net.URI

class ReferenceResolverRefSpec: StringSpec ({

    "resolves relative \$ref" {
        val openapi = """
            ${'$'}ref: foo.yaml#/foo
            """.trimIndent()

        val foo = """
            foo: FOO!
        """.trimIndent()

        Memory.add("/openapi.yaml", openapi)
        Memory.add("/foo.yaml", foo)

        val uri = URI("memory:/openapi.yaml")
        val resolver = ReferenceResolver(uri, UriReader(), JacksonConverter(), ReferenceRegistry())

        resolver.resolve()

        val ref = resolver.resolve(uri, "foo.yaml#/foo")
        val value = ref.documentValue
        value shouldBe "FOO!"
    }

    "resolves reference" {
        forAll(
            row(URI("memory:/openapi.yaml"), URI("memory:/foo.yaml"), "foo.yaml#/foo"),
            row(URI("file:/openapi.yaml"), URI("file:/foo.yaml"), "foo.yaml#/foo"),
        ) { parentUri, docUri, ref ->
            val registry = ReferenceRegistry()
            val resolver = ReferenceResolver(parentUri, mockk(), mockk(), registry)
            registry.add(parentUri, docUri, ref)

            val reference = resolver.resolve(parentUri, ref)
            reference.shouldNotBeNull()
        }
    }

    afterEach {
        Memory.clear()
    }

})

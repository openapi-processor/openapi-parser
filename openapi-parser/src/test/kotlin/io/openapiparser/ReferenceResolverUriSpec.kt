package io.openapiparser

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.openapiparser.jackson.JacksonConverter
import io.openapiparser.support.Memory
import java.net.URI

class ReferenceResolverUriSpec: StringSpec ({

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
        val registry = ReferenceRegistry(uri)
        val resolver = ReferenceResolver(uri, UriReader(), JacksonConverter(), registry)

        resolver.resolve()

        val nodeRef = registry.getRef("memory:/foo.yaml#/foo")
        val value = nodeRef.rawValue
        value shouldBe "FOO!"
    }

    afterEach {
        Memory.clear()
    }

})

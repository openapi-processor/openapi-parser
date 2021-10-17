package io.openapiparser.model.v30

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.openapiparser.support.TestBuilder

class PathItemSpec : StringSpec({

    "gets \$ref path item object" {
        val api = TestBuilder()
            .withApi("""
                paths:
                  /foo:
                    ${'$'}ref: ref path
            """.trimIndent())
            .buildOpenApi30()

        val path = api.paths.getPathItem("/foo")
        path.ref shouldBe "ref path"
        // todo other properties should be null
    }

})

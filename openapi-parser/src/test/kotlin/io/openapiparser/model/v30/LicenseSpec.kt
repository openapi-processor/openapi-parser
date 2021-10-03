package io.openapiparser.model.v30

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.openapiparser.support.TestBuilder

class LicenseSpec : StringSpec({

    "gets license object" {
        val api = TestBuilder()
            .withApi("""
                openapi: 3.0.3
                info:
                  title: the title
                  license:
                    name: license name
                    url: https://license
                  version: "1"
                paths: {}
            """.trimIndent())
            .buildOpenApi30()

        val license = api.info.license
        license.name shouldBe "license name"
        license.url shouldBe "https://license"
    }

})

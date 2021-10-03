package io.openapiparser.model.v30

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.openapiparser.support.TestBuilder

class ContactSpec : StringSpec({

    "gets contact object" {
        val api = TestBuilder()
            .withApi("""
                openapi: 3.0.3
                info:
                  title: the title
                  contact:
                    name: contact name
                    url: https://contact.url
                    email: contact@email
                  version: "1"
                paths: {}
            """.trimIndent())
            .buildOpenApi30()

        val contact = api.info.contact
        contact.name shouldBe "contact name"
        contact.url shouldBe "https://contact.url"
        contact.email shouldBe "contact@email"
    }

})

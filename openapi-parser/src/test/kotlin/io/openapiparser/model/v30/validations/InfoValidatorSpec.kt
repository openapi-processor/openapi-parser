package io.openapiparser.model.v30.validations

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldExist
import io.kotest.matchers.shouldBe
import io.openapiparser.ValidationContext
import io.openapiparser.support.TestBuilder
import io.openapiparser.support.matches
import java.net.URI

class InfoValidatorSpec : StringSpec({

    "reports errors of 'info' object" {
        val ctx = TestBuilder()
            .withApi("""
                bad: not allowed
            """.trimIndent())
            .buildContext()
        ctx.read()

        val validator = InfoValidator()
        val messages = validator.validate(
            ValidationContext(URI("file:///any"), "$.info"), ctx.baseNode)

        messages.size shouldBe 3

        // required
        messages.shouldExist { it.matches("$.info.title", "'title'") }
        messages.shouldExist { it.matches("$.info.version", "'version'") }

        // not allowed
        messages.shouldExist { it.matches("$.info.bad", "'bad'") }
    }

    "validates 'info' object" {
        val ctx = TestBuilder()
            .withApi("""
                title: a title
                description: a description
                termsOfService: https://terms.of.service
                contact: {}
                license: {}
                version: 1
                x-foo: allowed
            """.trimIndent())
            .buildContext()
        ctx.read()

        val validator = InfoValidator()
        val messages = validator.validate(
            ValidationContext(URI("file:///any"), "$.info"), ctx.baseNode)

        messages.shouldBeEmpty()
    }

})


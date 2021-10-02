package io.openapiparser.model.v30.validations

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldExist
import io.kotest.matchers.shouldBe
import io.openapiparser.ValidationContext
import io.openapiparser.support.TestBuilder
import io.openapiparser.support.matches
import java.net.URI

class OpenapiValidatorSpec : StringSpec({

    "reports errors in 'openapi' object" {
        val ctx = TestBuilder()
            .withApi("""
                openapi: 3
                x-foo: allowed
                bad: not allowed
            """.trimIndent())
            .buildContext()
        ctx.read()

        val validator = OpenapiValidator()
        val messages = validator.validate(
            ValidationContext(URI("file:///any")), ctx.baseNode)

        messages.size shouldBe 4
        // version
        messages.shouldExist { it.matches("$.openapi", "'3'") }
        // required
        messages.shouldExist { it.matches("$.info", "'info'") }
        messages.shouldExist { it.matches("$.paths", "'paths'") }
        // not allowed
        messages.shouldExist { it.matches("$.bad", "'bad'") }
    }

    "accepts valid 'openapi' object" {
        val ctx = TestBuilder()
            .withApi("""
                openapi: 3.0.3
                info: {}
                paths: {}
                x-foo: allowed
            """.trimIndent())
            .buildContext()
        ctx.read()

        val validator = OpenapiValidator()
        val messages = validator.validate(
            ValidationContext(URI("file:///any")), ctx.baseNode)

        messages.shouldBeEmpty()
    }

})

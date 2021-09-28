package io.openapiparser.validations

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldExist
import io.kotest.matchers.shouldBe
import io.openapiparser.Node
import io.openapiparser.ValidationContext
import io.openapiparser.support.matches
import io.openapiparser.validations.AllowedPropertiesValidator.Extensions.EXCLUDE_X
import io.openapiparser.validations.AllowedPropertiesValidator.Extensions.INCLUDE_X
import java.net.URI

class AllowedPropertiesValidatorSpec : StringSpec({

    "validates allowed properties of node" {
        val ctx = ValidationContext(URI("file:///any"))
        val required = AllowedPropertiesValidator(listOf("foo", "bar"))

        val messages = required.validate(
            ctx, Node(
                mapOf<String, Any>(
                    "foo" to "allowed",
                    "bar" to "allowed",
                    "oof" to "any",
                    "rab" to "any"
                )
            )
        )

        messages.size shouldBe 2
        messages.shouldExist { it.matches("$.oof", "'oof'") }
        messages.shouldExist { it.matches("$.rab", "'rab'") }
    }

    "validates allowed properties including extension properties of node" {
        val ctx = ValidationContext(URI("file:///any"))
        val required = AllowedPropertiesValidator(listOf("foo", "bar"), INCLUDE_X)

        val messages = required.validate(
            ctx, Node(
                mapOf<String, Any>(
                    "foo" to "allowed",
                    "bar" to "allowed",
                    "oof" to "any",
                    "rab" to "any",
                    "x-foo" to "allowed",
                    "x-bar" to "allowed"
                )
            )
        )

        messages.size shouldBe 2
        messages.shouldExist { it.matches("$.oof", "'oof'") }
        messages.shouldExist { it.matches("$.rab", "'rab'") }
    }

    "validates allowed properties excluding extension properties of node" {
        val ctx = ValidationContext(URI("file:///any"))
        val required = AllowedPropertiesValidator(listOf("foo", "bar"), EXCLUDE_X)

        val messages = required.validate(
            ctx, Node(
                mapOf<String, Any>(
                    "foo" to "allowed",
                    "bar" to "allowed",
                    "x-foo" to "allowed",
                    "x-bar" to "allowed"
                )
            )
        )

        messages.size shouldBe 2
        messages.shouldExist { it.matches("$.x-foo", "'x-foo'") }
        messages.shouldExist { it.matches("$.x-bar", "'x-bar'") }
    }

})

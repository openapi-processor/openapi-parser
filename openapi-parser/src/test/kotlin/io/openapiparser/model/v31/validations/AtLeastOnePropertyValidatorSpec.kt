package io.openapiparser.model.v31.validations

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldExist
import io.kotest.matchers.shouldBe
import io.openapiparser.Node
import io.openapiparser.ValidationContext
import io.openapiparser.support.matches
import java.net.URI

class AtLeastOnePropertyValidatorSpec : StringSpec({

    "warns at least one missing property" {
        val ctx = ValidationContext(URI("file:///any"))
        val required = AtLeastOnePropertyValidator(listOf("foo", "bar"))

        val messages = required.validate(ctx, Node(mapOf<String, Any>()))

        messages.size shouldBe 1
        messages.shouldExist { it.matches("$.(foo|bar)", "'foo|bar'") }
    }

    "does not warn if property exists" {
        val ctx = ValidationContext(URI("file:///any"))
        val required = AtLeastOnePropertyValidator(listOf("foo", "bar"))

        val messages = required.validate(ctx, Node(mapOf<String, Any>(
            "foo" to "any"
        )))

        messages.size shouldBe 0
    }

})

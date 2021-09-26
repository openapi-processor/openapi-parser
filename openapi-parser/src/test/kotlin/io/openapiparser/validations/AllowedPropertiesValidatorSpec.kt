package io.openapiparser.validations

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotContain
import io.openapiparser.Node
import io.openapiparser.ValidationContext
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

        val messageOof = ArrayList(messages)[0]
        messageOof.text shouldContain "'oof'"
        messageOof.path shouldBe "$.oof"

        val messageRab = ArrayList(messages)[1]
        messageRab.text shouldContain "'rab'"
        messageRab.path shouldBe "$.rab"
    }

    "validates allowed properties including extension properties of node" {
        val ctx = ValidationContext(URI("file:///any"))
        val required = AllowedPropertiesValidator(listOf("foo", "bar"), true)

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

        val message0 = ArrayList(messages)[0]
        message0.text shouldNotContain "'x-foo'"
        message0.text shouldNotContain "'x-bar'"

        val message1 = ArrayList(messages)[1]
        message1.text shouldNotContain "'x-foo'"
        message1.text shouldNotContain "'x-bar'"
    }

    "validates allowed properties excluding extension properties of node" {
        val ctx = ValidationContext(URI("file:///any"))
        val required = AllowedPropertiesValidator(listOf("foo", "bar"), false)

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

        val messageFoo = ArrayList(messages)[0]
        messageFoo.text shouldContain "'x-foo'"
        messageFoo.path shouldBe "$.x-foo"

        val messageBar = ArrayList(messages)[1]
        messageBar.text shouldContain "'x-bar'"
        messageBar.path shouldBe "$.x-bar"
    }

})

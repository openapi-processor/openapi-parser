/*
 * Copyright 2023 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.support

import io.kotest.core.spec.style.StringSpec
import io.kotest.data.blocking.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe

class EmailValidatorSpec : StringSpec({

    forAll(
        row("joe.bloggs@example.com", "a valid e-mail address", true),
        row("\"joe@bloggs\"@example.com", "a quoted string with a @ in the local part", true)
    ) { email, description, valid  ->
        "<$email> - $description" {
            EmailValidator(email).validate().shouldBe(valid)
        }
    }

})

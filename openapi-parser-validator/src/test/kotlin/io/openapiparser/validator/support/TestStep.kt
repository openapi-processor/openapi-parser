/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.support

import io.openapiparser.validator.ValidationMessage
import io.openapiparser.validator.steps.ValidationStep

class TestStep(private val msgs: Collection<ValidationMessage>): ValidationStep {

    override fun isValid(): Boolean {
        return msgs.isEmpty()
    }

    override fun getSteps(): Collection<ValidationStep> {
        return listOf(this)
    }

    override fun getMessages(): Collection<ValidationMessage> {
        return msgs;
    }
}

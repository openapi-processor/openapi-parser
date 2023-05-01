/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.support

import io.openapiparser.schema.JsonPointer
import io.openapiparser.schema.UriSupport
import io.openapiparser.validator.Annotation
import io.openapiparser.validator.ValidationMessage
import io.openapiparser.validator.steps.ValidationStep
import java.net.URI

class TestStep(val valid: Boolean, private val steps: Collection<ValidationStep> = emptyList()): ValidationStep {

    override fun isValid(): Boolean {
        return valid
    }

    override fun getSteps(): Collection<ValidationStep> {
        return steps
    }

    override fun add(step: ValidationStep) {
        steps.plus(steps)
    }

    override fun getMessage(): ValidationMessage? {
        return null;
    }

    override fun getAnnotation(): Annotation? {
        return null;
    }

    override fun getAnnotations(keyword: String): Collection<Annotation> {
        return emptyList()
    }

    override fun getKeywordLocation(): JsonPointer {
        return JsonPointer.empty()
    }

    override fun getAbsoluteKeywordLocation(): URI {
        return UriSupport.emptyUri()
    }

    override fun getInstanceLocation(): JsonPointer {
        return JsonPointer.empty()
    }
}

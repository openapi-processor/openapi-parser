/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v3x

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.openapiprocessor.jsonschema.converter.NoValueException
import io.openapiparser.model.v30.externalDocumentation as externalDocumentation30
import io.openapiparser.model.v31.externalDocumentation as externalDocumentation31
import io.openapiparser.model.v32.externalDocumentation as externalDocumentation32

class ExternalDocumentationSpec: StringSpec({

    include(testDescription("externalDocumentation 30", ::externalDocumentation30) { it.description })
    include(testDescription("externalDocumentation 31", ::externalDocumentation31) { it.description })
    include(testDescription("externalDocumentation 32", ::externalDocumentation32) { it.description })

    "gets externalDocumentation url" {
        externalDocumentation30("url: https://a.url").url shouldBe "https://a.url"
        externalDocumentation31("url: https://a.url").url shouldBe "https://a.url"
        externalDocumentation32("url: https://a.url").url shouldBe "https://a.url"
    }

    "gets externalDocumentation url throws if it is missing" {
        shouldThrow<NoValueException> { externalDocumentation30().url }
        shouldThrow<NoValueException> { externalDocumentation31().url }
        shouldThrow<NoValueException> { externalDocumentation32().url }
    }

    include(testExtensions("externalDocumentation 30", ::externalDocumentation30) { it.extensions })
    include(testExtensions("externalDocumentation 31", ::externalDocumentation31) { it.extensions })
    include(testExtensions("externalDocumentation 32", ::externalDocumentation32) { it.extensions })
})

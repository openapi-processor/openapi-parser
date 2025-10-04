/*
 * Copyright 2025 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v32

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.openapiparser.model.v32.securityScheme as securityScheme32

class SecuritySchemeSpec: StringSpec({

    "gets security scheme oauth2 metadata url" {
        securityScheme32("oauth2MetadataUrl: metadata url").oauth2MetadataUrl shouldBe "metadata url"
    }

    "gets security scheme oauth2 metadata url is null if missing" {
        securityScheme32().oauth2MetadataUrl.shouldBeNull()
    }

    "gets security scheme deprecated" {
        securityScheme32("deprecated: true").deprecated.shouldBeTrue()
        securityScheme32("deprecated: false").deprecated.shouldBeFalse()
    }

    "gets security scheme deprecated is false if missing" {
        securityScheme32().deprecated.shouldBeFalse()
    }
})

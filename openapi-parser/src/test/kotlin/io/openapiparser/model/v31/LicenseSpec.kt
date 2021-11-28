/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v31

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe

/**
 * @see [io.openapiparser.model.v3x.LicenseSpec]
 */
class LicenseSpec : StringSpec({

    "gets license identifier" {
        license("identifier: Apache-2.0").identifier shouldBe "Apache-2.0"
    }

    "gets license identifier is null if missing" {
        license().identifier.shouldBeNull()
    }
})

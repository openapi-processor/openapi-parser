/*
 * Copyright 2025 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v32

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe

/**
 * @see [io.openapiparser.model.v31.DiscriminatorSpec], [io.openapiparser.model.v3x.DiscriminatorSpec]
 */
class DiscriminatorSpec: StringSpec ({

    "gets default mapping" {
        discriminator("defaultMapping: mapping").defaultMapping.shouldBe("mapping")
    }

    "gets default mapping is null if missing" {
        discriminator().defaultMapping.shouldBeNull()
    }
})

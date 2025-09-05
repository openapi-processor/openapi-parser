/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v31

import io.kotest.core.spec.style.StringSpec
import io.openapiparser.model.v3x.testExtensions
import io.openapiparser.model.v31.discriminator as discriminator31
import io.openapiparser.model.v32.discriminator as discriminator32

/**
 * @see [io.openapiparser.model.v3x.DiscriminatorSpec]
 */
class DiscriminatorSpec: StringSpec ({

    include(testExtensions("discriminator", ::discriminator31) { it.extensions })
    include(testExtensions("discriminator", ::discriminator32) { it.extensions })
})

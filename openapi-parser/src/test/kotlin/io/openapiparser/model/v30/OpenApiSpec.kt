/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v30

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.openapiparser.converter.NoValueException

/**
 * @see [io.openapiparser.model.v3x.OpenApiSpec]
 */
class OpenApiSpec : StringSpec({

    "gets paths object" {
        openapi("paths: {}").paths.shouldNotBeNull()
    }

    "gets path object throws if it missing" {
        shouldThrow<NoValueException> { openapi().paths }
    }
})


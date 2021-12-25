/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v31

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull

/**
 * @see [io.openapiparser.model.v3x.OperationSpec]
 */
class OperationSpec: StringSpec({

    "gets operation responses" {
        operation("responses: {}").responses.shouldNotBeNull()
    }

    "gets operation responses is null if missing" {
        operation().responses.shouldBeNull()
    }

})

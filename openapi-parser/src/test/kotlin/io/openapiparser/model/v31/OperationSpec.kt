/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v31

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.openapiparser.model.v31.operation as operation31
import io.openapiparser.model.v31.operation as operation32

/**
 * @see [io.openapiparser.model.v3x.OperationSpec]
 */
class OperationSpec: StringSpec({

    "gets operation responses" {
        operation31("responses: {}").responses.shouldNotBeNull()
        operation32("responses: {}").responses.shouldNotBeNull()
    }

    "gets operation responses is null if missing" {
        operation31().responses.shouldBeNull()
        operation32().responses.shouldBeNull()
    }
})

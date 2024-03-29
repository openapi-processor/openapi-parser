/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v30

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.openapiprocessor.jsonschema.converter.NoValueException

/**
 * @see [io.openapiparser.model.v3x.OperationSpec]
 */
class OperationSpec: StringSpec({

    "gets operation responses" {
        operation("responses: {}").responses.shouldNotBeNull()
    }

    "gets operation responses throws if missing" {
        shouldThrow<NoValueException> { operation().responses }
    }

})

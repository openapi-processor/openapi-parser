/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator

import io.kotest.core.spec.style.FreeSpec

class JsonSchemaPendingSpec: FreeSpec({
    include(draftSpec("/suites/pending"))
})

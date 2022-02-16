/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator

import io.kotest.core.spec.style.FreeSpec
import io.openapiparser.validator.support.draftSpec

class JsonSchemaTestSuiteSpec: FreeSpec ({
    include(draftSpec("/suites/JSON-Schema-Test-Suite/tests/draft4", arrayOf(
        "ecmascript-regex.json"  // (optional) not supported
    )))
})

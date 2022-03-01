/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator

import io.kotest.core.spec.style.FreeSpec
import io.openapiparser.validator.support.draftSpec

class JsonSchemaTestSuiteSpec: FreeSpec ({
    include(draftSpec("/suites/JSON-Schema-Test-Suite/tests/draft4", arrayOf(
        // (optional) ecmascript-regex.json, too many fails to list them
        "ecmascript-regex.json",
        // (optional) date-time.json, supports all except leap second
        "a valid date-time with a leap second, UTC",
        "a valid date-time with a leap second, with minus offset",
    )))
})

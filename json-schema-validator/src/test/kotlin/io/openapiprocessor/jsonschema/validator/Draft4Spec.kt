/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator

import io.kotest.core.spec.style.FreeSpec
import io.openapiprocessor.jsonschema.validator.support.Exclude
import io.openapiprocessor.jsonschema.validator.support.draftSpec

class Draft4Spec: FreeSpec ({
    include(
        draftSpec(
        "/suites/JSON-Schema-Test-Suite/tests/draft4",
        ValidatorSettingsDefaults.draft4(),
        draft4Extras)
    )
})

val draft4Extras = listOf(
    // (optional) ecmascript-regex.json, too many fails to list them
    Exclude("ecmascript-regex.json"),
    // (optional) date-time.json, supports all except leap second
    Exclude("a valid date-time with a leap second, UTC"),
    Exclude("a valid date-time with a leap second, with minus offset"),
    // todo
    Exclude("empty tokens in \$ref json-pointer")
)

/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator

import io.kotest.core.spec.style.FreeSpec
import io.openapiprocessor.jsonschema.validator.support.Exclude
import io.openapiprocessor.jsonschema.validator.support.draftSpec

class Draft6Spec: FreeSpec ({
    include(
        draftSpec(
        "/suites/JSON-Schema-Test-Suite/tests/draft6",
        ValidatorSettingsDefaults.draft6(),
        draft6Extras
    )
    )
})

val draft6Extras = listOf(
    // (optional) json-pointer.json, not implemented
    Exclude("json-pointer.json"),
    // (optional) uri-template.json, not implemented
    Exclude("uri-template.json"),
    // (optional) ecmascript-regex.json, too many fails to list them
    Exclude("ecmascript-regex.json"),
    // (optional) date-time.json, supports all except leap second
    Exclude("a valid date-time with a leap second, UTC"),
    Exclude("a valid date-time with a leap second, with minus offset"),
    // todo
    Exclude("empty tokens in \$ref json-pointer")
)

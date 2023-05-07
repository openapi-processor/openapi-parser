/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator

import io.kotest.core.spec.style.FreeSpec
import io.openapiparser.validator.support.*

class Draft7Spec: FreeSpec ({
    include(draftSpec(
        "/suites/JSON-Schema-Test-Suite/tests/draft7",
        ValidatorSettingsDefaults.draft7(),
        draft7Extras
    ))
})

val draft7Extras = listOf(
    // (optional format) not implemented
    Exclude("idn-email.json"),
    // (optional format) not implemented
    Exclude("idn-hostname.json"),
    // (optional format) not implemented
    Exclude("iri.json"),
    // (optional format) not implemented
    Exclude("iri-reference.json"),
    // (optional format) not implemented
    Exclude("json-pointer.json"),
    // (optional format) not implemented
    Exclude("relative-json-pointer.json"),
    // (optional format) not implemented
    Exclude("uri-template.json"),
    // (optional) content.json, not implemented
    Exclude("content.json"),
    // (optional) ecmascript-regex.json, too many fails to list them
    Exclude("ecmascript-regex.json"),
    // (optional) date-time.json, supports all except leap second
    Exclude("a valid date-time with a leap second, UTC"),
    Exclude("a valid date-time with a leap second, with minus offset"),
    // (optional) time.json, supports all except leap second
    Exclude("a valid time string with leap second, Zulu"),
    Exclude("valid leap second, zero time-offset"),
    Exclude("valid leap second, positive time-offset"),
    Exclude("valid leap second, large positive time-offset"),
    Exclude("valid leap second, negative time-offset"),
    Exclude("valid leap second, large negative time-offset"),
    // todo
    Exclude("empty tokens in \$ref json-pointer")
)

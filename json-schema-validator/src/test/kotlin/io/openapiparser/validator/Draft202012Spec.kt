/*
 * Copyright 2023 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator

import io.kotest.core.annotation.Ignored
import io.kotest.core.spec.style.FreeSpec
import io.openapiparser.schema.Format
import io.openapiparser.validator.support.*

@Ignored
class Draft202012Spec: FreeSpec ({
    val settings = ValidatorSettingsDefaults.draft202012()
    settings.enableFormats(*Format.values())

    include(draftSpec(
        "/suites/JSON-Schema-Test-Suite/tests/draft2020-12",
        settings,
        draft202012Extras
    ))
})

val draft202012Extras = listOf(
    // (optional format) not implemented
    Exclude("duration.json"),
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
    Exclude("valid leap second, large negative time-offset")
)

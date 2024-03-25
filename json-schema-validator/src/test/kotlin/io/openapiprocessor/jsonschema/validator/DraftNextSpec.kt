/*
 * Copyright 2024 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator

import io.kotest.core.annotation.Ignored
import io.kotest.core.spec.style.FreeSpec
import io.openapiprocessor.jsonschema.schema.Format
import io.openapiprocessor.jsonschema.validator.support.Exclude
import io.openapiprocessor.jsonschema.validator.support.Settings
import io.openapiprocessor.jsonschema.validator.support.draftSpec

//@Ignored
class DraftNextSpec: FreeSpec ({
    val settings = ValidatorSettingsDefaults.draftNext()
    settings.enableFormat()
    settings.enableFormats(*Format.entries.toTypedArray())

    include(
        draftSpec(
        "/suites/JSON-Schema-Test-Suite/tests/draft-next",
        settings,
        draftNextExtras
    ))
})

val draftNextExtras = listOf(
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
    Exclude("valid leap second, large negative time-offset"),

    Settings("invalid email string is only an annotation by default", disableFormat()),
    Settings("invalid regex string is only an annotation by default", disableFormat()),
    Settings("invalid ipv4 string is only an annotation by default", disableFormat()),
    Settings("invalid ipv6 string is only an annotation by default", disableFormat()),
    Settings("invalid hostname string is only an annotation by default", disableFormat()),
    Settings("invalid date string is only an annotation by default", disableFormat()),
    Settings("invalid date-time string is only an annotation by default", disableFormat()),
    Settings("invalid time string is only an annotation by default", disableFormat()),
    Settings("invalid uri string is only an annotation by default", disableFormat()),
    Settings("invalid uri-reference string is only an annotation by default", disableFormat()),
    Settings("invalid uuid string is only an annotation by default", disableFormat())
)

private fun disableFormat() = { s: ValidatorSettings -> s.disableFormat(); s }

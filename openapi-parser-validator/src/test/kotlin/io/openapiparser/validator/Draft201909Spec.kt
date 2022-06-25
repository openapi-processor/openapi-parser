/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator

import io.kotest.core.annotation.Ignored
import io.kotest.core.spec.style.FreeSpec
import io.openapiparser.schema.Format
import io.openapiparser.validator.support.*

@Ignored
class Draft201909Spec: FreeSpec ({
    val settings = ValidatorSettingsDefaults.draft201909()
    settings.enableFormats(*Format.values())

    include(draftSpec(
        "/suites/JSON-Schema-Test-Suite/tests/draft2019-09",
        settings,
        draft201909Extras
    ))
})

val draft201909Extras = listOf(
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

    // remote documents
    Remote(
        "remote ref", Document(
            "http://localhost:1234/integer.json",
            "/suites/JSON-Schema-Test-Suite/remotes/integer.json"
        )
    ),
    Remote(
        "fragment within remote ref", Document(
            "http://localhost:1234/subSchemas-defs.json",
            "/suites/JSON-Schema-Test-Suite/remotes/subSchemas-defs.json"
        )
    ),
    Remote(
        "ref within remote ref", Document(
            "http://localhost:1234/subSchemas-defs.json",
            "/suites/JSON-Schema-Test-Suite/remotes/subSchemas-defs.json"
        )
    ),
    Remote(
        "base URI change", Document(
            "http://localhost:1234/baseUriChange/folderInteger.json",
            "/suites/JSON-Schema-Test-Suite/remotes/baseUriChange/folderInteger.json"
        )
    ),
    Remote(
        "base URI change - change folder", Document(
            "http://localhost:1234/baseUriChangeFolder/folderInteger.json",
            "/suites/JSON-Schema-Test-Suite/remotes/baseUriChangeFolder/folderInteger.json"
        )
    ),
    Remote(
        "base URI change - change folder in subschema", Document(
            "http://localhost:1234/baseUriChangeFolderInSubschema/folderInteger.json",
            "/suites/JSON-Schema-Test-Suite/remotes/baseUriChangeFolderInSubschema/folderInteger.json"
        )
    ),
    Remote(
        "root ref in remote ref", Document(
            "http://localhost:1234/name-defs.json",
            "/suites/JSON-Schema-Test-Suite/remotes/name-defs.json"
        )
    ),
    Remote(
        "remote ref with ref to defs", Document(
            "http://localhost:1234/ref-and-defs.json",
            "/suites/JSON-Schema-Test-Suite/remotes/ref-and-defs.json"
        )
    )
)

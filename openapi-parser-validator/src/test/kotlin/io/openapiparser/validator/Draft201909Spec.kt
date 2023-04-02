/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator

import io.kotest.core.annotation.Ignored
import io.kotest.core.spec.style.FreeSpec
import io.openapiparser.schema.Format
import io.openapiparser.validator.support.*

//@Ignored
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

    // "remote" documents
    Remote(
        "remote ref", Document(
            "http://localhost:1234/draft2019-09/integer.json",
            "/suites/JSON-Schema-Test-Suite/remotes/draft2019-09/integer.json"
        )
    ),
    Remote(
        "fragment within remote ref", Document(
            "http://localhost:1234/draft2019-09/subSchemas-defs.json",
            "/suites/JSON-Schema-Test-Suite/remotes/draft2019-09/subSchemas-defs.json"
        )
    ),
    Remote(
        "anchor within remote ref", Document(
            "http://localhost:1234/draft2019-09/locationIndependentIdentifier.json",
            "/suites/JSON-Schema-Test-Suite/remotes/draft2019-09/locationIndependentIdentifier.json"
        )
    ),
    Remote(
        "ref within remote ref", Document(
            "http://localhost:1234/draft2019-09/subSchemas-defs.json",
            "/suites/JSON-Schema-Test-Suite/remotes/draft2019-09/subSchemas-defs.json"
        )
    ),
    Remote(
        "base URI change", Document(
            "http://localhost:1234/draft2019-09/baseUriChange/folderInteger.json",
            "/suites/JSON-Schema-Test-Suite/remotes/draft2019-09/baseUriChange/folderInteger.json"
        )
    ),
    Remote(
        "base URI change - change folder", Document(
            "http://localhost:1234/draft2019-09/baseUriChangeFolder/folderInteger.json",
            "/suites/JSON-Schema-Test-Suite/remotes/draft2019-09/baseUriChangeFolder/folderInteger.json"
        )
    ),
    Remote(
        "base URI change - change folder in subschema", Document(
            "http://localhost:1234/draft2019-09/baseUriChangeFolderInSubschema/folderInteger.json",
            "/suites/JSON-Schema-Test-Suite/remotes/draft2019-09/baseUriChangeFolderInSubschema/folderInteger.json"
        )
    ),
    Remote(
        "root ref in remote ref", Document(
            "http://localhost:1234/draft2019-09/name-defs.json",
            "/suites/JSON-Schema-Test-Suite/remotes/draft2019-09/name-defs.json"
        )
    ),
    Remote(
        "remote ref with ref to defs", Document(
            "http://localhost:1234/draft2019-09/ref-and-defs.json",
            "/suites/JSON-Schema-Test-Suite/remotes/draft2019-09/ref-and-defs.json"
        )
    ),
    Remote(
        "Location-independent identifier in remote ref", Document(
            "http://localhost:1234/draft2019-09/locationIndependentIdentifier.json",
            "/suites/JSON-Schema-Test-Suite/remotes/draft2019-09/locationIndependentIdentifier.json"
        )
    ),
    Remote(
        "retrieved nested refs resolve relative to their URI not \$id", Document(
            "http://localhost:1234/draft2019-09/nested/foo-ref-string.json",
            "/suites/JSON-Schema-Test-Suite/remotes/draft2019-09/nested/foo-ref-string.json"
        )
    ),
    Remote(
        "retrieved nested refs resolve relative to their URI not \$id", Document(
            "http://localhost:1234/draft2019-09/nested/string.json",
            "/suites/JSON-Schema-Test-Suite/remotes/draft2019-09/nested/string.json"
        )
    ),
    Remote(
        "remote HTTP ref with different \$id", Document(
            "http://localhost:1234/different-id-ref-string.json",
            "/suites/JSON-Schema-Test-Suite/remotes/different-id-ref-string.json"
        )
    ),
    Remote(
        "remote HTTP ref with different URN \$id", Document(
            "http://localhost:1234/urn-ref-string.json",
            "/suites/JSON-Schema-Test-Suite/remotes/urn-ref-string.json"
        )
    ),
    Remote(
        "remote HTTP ref with nested absolute ref", Document(
            "http://localhost:1234/nested-absolute-ref-to-string.json",
            "/suites/JSON-Schema-Test-Suite/remotes/nested-absolute-ref-to-string.json"
        )
    ),
    Remote(
        "refs to future drafts are processed as future drafts", Document(
            "http://localhost:1234/draft2020-12/prefixItems.json",
            "/suites/JSON-Schema-Test-Suite/remotes/draft2020-12/prefixItems.json"
        )
    ),
    Remote(
        "refs to historic drafts are processed as historic drafts", Document(
            "http://localhost:1234/draft7/ignore-dependentRequired.json",
            "/suites/JSON-Schema-Test-Suite/remotes/draft7/ignore-dependentRequired.json"
        )
    ),
    Remote(
        "schema that uses custom metaschema with with no validation vocabulary", Document(
            "http://localhost:1234/draft2019-09/metaschema-no-validation.json",
            "/suites/JSON-Schema-Test-Suite/remotes/draft2019-09/metaschema-no-validation.json"
        )
    )
)

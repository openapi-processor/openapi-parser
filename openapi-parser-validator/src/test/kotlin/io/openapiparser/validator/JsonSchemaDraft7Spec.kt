/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator

import io.kotest.core.spec.style.FreeSpec
import io.openapiparser.schema.SchemaVersion
import io.openapiparser.validator.support.*

class JsonSchemaDraft7Spec: FreeSpec ({
    val settings = ValidatorSettings()
    settings.version = SchemaVersion.Draft7

    include(draftSpec(
        "/suites/JSON-Schema-Test-Suite/tests/draft7",
        settings,
        listOf(
            // (optional) json-pointer.json, not implemented
            Exclude("json-pointer.json"),
            // (optional) uri-template.json, not implemented
            Exclude("uri-template.json"),
            // (optional) ecmascript-regex.json, too many fails to list them
            Exclude("ecmascript-regex.json"),
            // (optional) date-time.json, supports all except leap second
            Exclude("a valid date-time with a leap second, UTC"),
            Exclude("a valid date-time with a leap second, with minus offset"),

            // remote documents
            Remote(
                "remote ref", Document(
                    "http://localhost:1234/integer.json",
                    "/suites/JSON-Schema-Test-Suite/remotes/integer.json"
                )
            ),
            Remote(
                "fragment within remote ref", Document(
                    "http://localhost:1234/subSchemas.json",
                    "/suites/JSON-Schema-Test-Suite/remotes/subSchemas.json"
                )
            ),
            Remote(
                "ref within remote ref", Document(
                    "http://localhost:1234/subSchemas.json",
                    "/suites/JSON-Schema-Test-Suite/remotes/subSchemas.json"
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
                    "http://localhost:1234/name.json",
                    "/suites/JSON-Schema-Test-Suite/remotes/name.json"
                )
            ),
            Remote(
                "remote ref with ref to definitions", Document(
                    "http://localhost:1234/ref-and-definitions.json",
                    "/suites/JSON-Schema-Test-Suite/remotes/ref-and-definitions.json"
                )
            )
        )
    ))
})

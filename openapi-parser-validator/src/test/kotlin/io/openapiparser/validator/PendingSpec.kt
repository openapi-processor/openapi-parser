/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator

import io.kotest.core.annotation.Ignored
import io.kotest.core.spec.style.FreeSpec
import io.openapiparser.schema.Format
import io.openapiparser.schema.SchemaVersion
import io.openapiparser.validator.support.draftSpec

@Ignored
class PendingSpec : FreeSpec({
//    val settings = ValidatorSettingsDefaults.draft4()
//    val settings = ValidatorSettingsDefaults.draft7()
//    val settings = ValidatorSettingsDefaults.draft201909()
    val settings = ValidatorSettingsDefaults.draft202012()
    settings.enableFormats(*Format.values())
//    settings.version = SchemaVersion.Draft4
//    settings.version = SchemaVersion.Draft6
//    settings.version = SchemaVersion.Draft7
//    settings.version = SchemaVersion.Draft201909
    settings.version = SchemaVersion.Draft202012

    include(draftSpec(
        "/suites/pending",
        settings,
//        draft4Extras
//        draft6Extras
//        draft7Extras
//        draft201909Extras
        draft202012Extras
    ))
})

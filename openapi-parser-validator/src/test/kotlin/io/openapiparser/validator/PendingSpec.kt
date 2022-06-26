/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator

import io.kotest.core.annotation.Ignored
import io.kotest.core.spec.style.FreeSpec
import io.openapiparser.schema.SchemaVersion
import io.openapiparser.validator.support.draftSpec

//@Ignored
class PendingSpec : FreeSpec({
    val settings = ValidatorSettings()
//    settings.version = SchemaVersion.Draft4
    settings.version = SchemaVersion.Draft201909

    include(draftSpec(
        "/suites/pending",
        settings,
//        draft4Extras
        draft201909Extras
    ))
})

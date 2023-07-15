/*
 * Copyright 2023 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.openapiprocessor.jsonschema.schema.Format

class ValidatorSettingsSpec : StringSpec({

    "draft 201909: assertFormat is always false if format assertion is disabled globally" {
        val settings = ValidatorSettingsDefaults.draft201909()
        settings.enableFormats(Format.DATE)

        settings.assertFormat().shouldBeFalse()
        settings.assertFormat(Format.DATE).shouldBeFalse()
    }

    "draft 201909: assertFormat is false by default" {
        val settings = ValidatorSettingsDefaults.draft201909()
        settings.assertFormat().shouldBeFalse()
    }

    "draft 7: assertFormat is true by default" {
        val settings = ValidatorSettingsDefaults.draft7()
        settings.assertFormat().shouldBeTrue()
    }

    "draft 6: assertFormat is true by default" {
        val settings = ValidatorSettingsDefaults.draft6()
        settings.assertFormat().shouldBeTrue()
    }

    "draft 4: assertFormat is true by default" {
        val settings = ValidatorSettingsDefaults.draft4()
        settings.assertFormat().shouldBeTrue()
    }

})

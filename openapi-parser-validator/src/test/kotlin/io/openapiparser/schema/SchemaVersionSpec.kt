/*
 * Copyright 2023 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue

class SchemaVersionSpec : StringSpec({

    "is before" {
        SchemaVersion.Draft202012.isBefore202012.shouldBeFalse()
        SchemaVersion.Draft201909.isBefore202012.shouldBeTrue()
        SchemaVersion.Draft7.isBefore202012.shouldBeTrue()
        SchemaVersion.Draft6.isBefore202012.shouldBeTrue()
        SchemaVersion.Draft4.isBefore202012.shouldBeTrue()

        SchemaVersion.Draft201909.isBefore201909.shouldBeFalse()
        SchemaVersion.Draft7.isBefore201909.shouldBeTrue()
        SchemaVersion.Draft6.isBefore201909.shouldBeTrue()
        SchemaVersion.Draft4.isBefore201909.shouldBeTrue()
    }

})

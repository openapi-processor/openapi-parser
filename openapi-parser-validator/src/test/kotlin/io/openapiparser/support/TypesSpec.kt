/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.support

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.openapiparser.converter.Types.`as`
import io.openapiparser.converter.Types.asMap

class TypesSpec: StringSpec({

    "casting null to map" {
        asMap(null).shouldBeNull()
        `as`<Map<String, String>?>(null).shouldBeNull()
    }

    "casting object to map" {
        asMap(emptyMap<String, String>() as Any).shouldNotBeNull()
        `as`<Map<String, String>?>(emptyMap<String, String>() as Any).shouldNotBeNull()
    }
})

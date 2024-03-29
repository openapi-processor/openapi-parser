/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.support

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.openapiprocessor.jsonschema.support.Types.*

class TypesSpec: StringSpec({

    "casts null to string" {
        asString(null).shouldBeNull()
    }

    "casts null to map" {
        asMap(null).shouldBeNull()
    }

    "casts object to map" {
        asMap(emptyMap<String, String>() as Any).shouldNotBeNull()
    }

    "casts null to object" {
        asObject(null).shouldBeNull()
    }

    "casts object to object" {
        asObject(emptyMap<String, String>() as Any).shouldNotBeNull()
    }
})

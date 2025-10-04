/*
 * Copyright 2025 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v32

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

/**
 * @see [io.openapiparser.model.v3x.ComponentsSpec]
 */
class ComponentsSpec: StringSpec ({

    fun assertMap (items: Map<String, MediaType>) {
        items.size shouldBe 2
        items["foo"].shouldNotBeNull()
        items["bar"].shouldNotBeNull()
    }

    "gets components mediaTypes" {
        val source = """
          mediaTypes:
            foo: {}
            bar: {}
        """

        assertMap(components(source).mediaTypes)
    }

    "gets components mediaTypes is empty if missing" {
        components().mediaTypes.shouldBeEmpty()
    }
})

/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v31

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.openapiparser.model.v31.components as components31
import io.openapiparser.model.v32.components as components32

/**
 * @see [io.openapiparser.model.v3x.ComponentsSpec]
 */
class ComponentsSpec: StringSpec ({

    fun assertPathItems (pathItems: Map<String, Any?>) {
        pathItems.size shouldBe 2
        pathItems["/foo"].shouldNotBeNull()
        pathItems["/bar"].shouldNotBeNull()
    }

    "gets components pathItems" {
        val source = """
          pathItems:
            /foo: {}
            /bar: {}
        """

        assertPathItems(components31(source).pathItems)
        assertPathItems(components32(source).pathItems)
    }

    "gets components pathItems is empty if missing" {
        components31().pathItems.shouldBeEmpty()
        components32().pathItems.shouldBeEmpty()
    }
})

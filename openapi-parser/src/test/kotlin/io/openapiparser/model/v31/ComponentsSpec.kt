/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v31

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

/**
 * @see [io.openapiparser.model.v3x.ComponentsSpec]
 */
class ComponentsSpec: StringSpec ({

    "gets components pathItems" {
        val source = """
          pathItems:
            foo: {}
            bar: {}
        """

        val pathItems = components(source).pathItems

        pathItems.size shouldBe 2
        pathItems["foo"].shouldNotBeNull()
        pathItems["bar"].shouldNotBeNull()
    }

    "gets components pathItems is empty if missing" {
        components().pathItems.shouldBeEmpty()
    }

})

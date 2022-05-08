/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v3x

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.openapiparser.model.v30.callback as callback30
import io.openapiparser.model.v31.callback as callback31

class CallbackSpec: StringSpec ({

    "gets callback path item" {
        val source = """
          /foo: {}
        """

        callback30(source).getPathItem("/foo").shouldNotBeNull()
        callback31(source).getPathItem("/foo").shouldNotBeNull()
    }

    fun assertPathItems (pathItems: Map<String, Any>) {
        pathItems.size shouldBe 2
        pathItems["/foo"].shouldNotBeNull()
        pathItems["/bar"].shouldNotBeNull()
    }

    "gets callback path items" {
        val source = """
          /foo: {}
          /bar: {}
        """

        assertPathItems(callback30(source).pathItems)
        assertPathItems(callback31(source).pathItems)
    }

    include(testExtensions("Callback 30", ::callback30) { it.extensions })
    include(testExtensions("Callback 31", ::callback31) { it.extensions })
})

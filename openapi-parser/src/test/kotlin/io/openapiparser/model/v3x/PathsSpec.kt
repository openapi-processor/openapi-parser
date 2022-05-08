/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v3x

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.openapiparser.model.v30.paths as paths30
import io.openapiparser.model.v31.paths as paths31

class PathsSpec: StringSpec({

    "gets path item object" {
        val source = """
          /foo: {}
        """

        paths30(source).getPathItem("/foo").shouldNotBeNull()
        paths31(source).getPathItem("/foo").shouldNotBeNull()
    }

    "gets path items objects" {
        val source = """
          /foo: {}
          /bar: {}
        """

        val p30 = paths30(source).pathItems
        p30.size shouldBe 2
        p30["/foo"].shouldNotBeNull()
        p30["/bar"].shouldNotBeNull()

        val p31 = paths31(source).pathItems
        p31.size shouldBe 2
        p31["/foo"].shouldNotBeNull()
        p31["/bar"].shouldNotBeNull()
    }

    include(testExtensions("Paths30", ::paths30) { it.extensions })
    include(testExtensions("Paths31", ::paths31) { it.extensions })
})

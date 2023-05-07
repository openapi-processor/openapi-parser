/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.string.shouldBeEmpty
import java.net.URI

class UriSpec: StringSpec({

    "detects fragment" {
        val noFragment = URI("")
        noFragment.fragment.shouldBeNull()

        val emptyFragment = URI("#")
        emptyFragment.fragment.shouldBeEmpty()
    }

    "adds empty fragment" {
        val noFragment = URI("")
        val emptyFragment = noFragment.resolve("#")
        emptyFragment.fragment.shouldBeEmpty()
    }
})

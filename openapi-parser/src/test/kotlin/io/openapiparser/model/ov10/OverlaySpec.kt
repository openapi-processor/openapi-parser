/*
 * Copyright 2024 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.ov10

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.openapiparser.model.v3x.testExtensions
import io.openapiprocessor.jsonschema.converter.NoValueException

class OverlaySpec: StringSpec({

    "get overlay version" {
        overlay("overlay: 1.0.0").overlay shouldBe "1.0.0"
    }

    "get overlay version throws if it is missing" {
        shouldThrow<NoValueException> { overlay().overlay }
    }

    "gets info object" {
        overlay("info: {}").info.shouldNotBeNull()
    }

    "gets info object throws if it missing" {
        shouldThrow<NoValueException> { overlay().info }
    }

    "get extends url" {
        overlay("extends: https://a.url").extends shouldBe "https://a.url"
    }

    "get extends url is null if it is missing" {
        overlay().extends.shouldBeNull()
    }

    "get action objects" {
        val actions = overlay("actions: [{}, {}]").actions
        actions.shouldNotBeNull()
        actions.size shouldBe 2
    }

    "get action object throws if it is missing" {
        shouldThrow<NoValueException> { overlay().actions }
    }

    "get action object throws if it is empty" {
        shouldThrow<NoValueException> { overlay("actions: []").actions }
    }

    include(testExtensions("Overlay", ::overlay) { it.extensions })
})

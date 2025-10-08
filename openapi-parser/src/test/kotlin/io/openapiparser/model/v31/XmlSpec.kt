/*
 * Copyright 2025 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v31

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.openapiparser.model.v31.xml as xml31
import io.openapiparser.model.v32.xml as xml32

@Suppress("DEPRECATION")
class XmlSpec: StringSpec({

    "gets xml name" {
        xml31("name: the name").name shouldBe "the name"
        xml32("name: the name").name shouldBe "the name"
    }

    "gets xml name is null if missing" {
        xml31().name.shouldBeNull()
        xml32().name.shouldBeNull()
    }

    "gets xml namespace" {
        xml31("namespace: the namespace").namespace shouldBe "the namespace"
        xml32("namespace: the namespace").namespace shouldBe "the namespace"
    }

    "gets xml namespace is null if missing" {
        xml31().namespace.shouldBeNull()
        xml32().namespace.shouldBeNull()
    }

    "gets xml prefix" {
        xml31("prefix: the prefix").prefix shouldBe "the prefix"
        xml32("prefix: the prefix").prefix shouldBe "the prefix"
    }

    "gets xml prefix is null if missing" {
        xml31().prefix.shouldBeNull()
        xml32().prefix.shouldBeNull()
    }

    "gets xml attribute" {
        xml31("attribute: true").attribute.shouldBeTrue()
        xml31("attribute: false").attribute.shouldBeFalse()
        xml32("attribute: true").attribute.shouldBeTrue()
        xml32("attribute: false").attribute.shouldBeFalse()
    }

    "gets xml attribute is false if missing" {
        xml31().attribute.shouldBeFalse()
        xml32().attribute.shouldBeFalse()
    }

    "gets xml wrapped" {
        xml31("wrapped: true").wrapped.shouldBeTrue()
        xml31("wrapped: false").wrapped.shouldBeFalse()
        xml32("wrapped: true").wrapped.shouldBeTrue()
        xml32("wrapped: false").wrapped.shouldBeFalse()
    }

    "gets xml wrapped is false if missing" {
        xml31().wrapped.shouldBeFalse()
        xml32().wrapped.shouldBeFalse()
    }
})

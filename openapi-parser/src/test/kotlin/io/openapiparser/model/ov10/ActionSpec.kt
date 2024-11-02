/*
 * Copyright 2024 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.ov10

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.openapiparser.model.v3x.testExtensions
import io.openapiprocessor.jsonschema.converter.NoValueException
import io.openapiprocessor.jsonschema.converter.TypeMismatchException
import io.openapiparser.model.v31.Info as Info31

class ActionSpec: StringSpec({

    "get target" {
        action("target: $.info").target shouldBe "$.info"
    }

    "get target throws if it is missing" {
        shouldThrow<NoValueException> { action().target }
    }

    "get action description" {
        action("description: description").description shouldBe "description"
    }

    "gets action description is null if it is missing" {
        action().description.shouldBeNull()
    }

    "get update" {
        action("update: {}").update.shouldNotBeNull()
    }

    "get update is null if it is missing" {
        action().update.shouldBeNull()
    }

    "get update as type" {
        action("update: {title: the title}").getUpdate(Info31::class.java).shouldNotBeNull()
    }

    "get update as type throws if conversion fails" {
        shouldThrow<TypeMismatchException> {
            action("update: false").getUpdate(Info31::class.java)
        }
    }

    "get remove" {
        action("remove: true").remove!!.shouldBeTrue()
    }

    "get remove is null if it is missing" {
        action().remove.shouldBeNull()
    }

    include(testExtensions("Action", ::action) { it.extensions })
})

/*
 * Copyright 2025 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v31

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.openapiprocessor.jsonschema.converter.NoValueException
import io.openapiparser.model.v31.tag as tag31
import io.openapiparser.model.v32.tag as tag32

class TagSpec: StringSpec({

    "gets tag name" {
        tag31("name: the name").name shouldBe "the name"
        tag32("name: the name").name shouldBe "the name"
    }

    "gets tag name is null if missing" {
        shouldThrow<NoValueException> { tag31().name }
        shouldThrow<NoValueException> { tag32().name }
    }

    "gets tag description" {
        tag31("description: a description").description shouldBe "a description"
        tag32("description: a description").description shouldBe "a description"
    }

    "gets tag description is null if missing" {
        tag31().description.shouldBeNull()
        tag32().description.shouldBeNull()
    }

    "gets external docs object" {
        tag31("externalDocs: {}").externalDocs.shouldNotBeNull()
        tag32("externalDocs: {}").externalDocs.shouldNotBeNull()
    }

    "gets external docs object is null if it missing" {
        tag31().externalDocs.shouldBeNull()
        tag32().externalDocs.shouldBeNull()
    }
})

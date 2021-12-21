/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v30

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.openapiparser.NoValueException

/**
 * @see [io.openapiparser.model.v3x.SchemaSpec]
 */
class SchemaSpec: StringSpec({

    // todo
    // discriminator
    // xml
    // externalDocs
    // example

    "gets schema type" {
        schema("type: string").type shouldBe "string"
    }

    "gets schema type throws if missing" {
        shouldThrow<NoValueException> { schema().type }
    }

    "gets schema nullable" {
        schema("nullable: true").nullable.shouldBeTrue()
    }

    "gets schema nullable is false if missing" {
        schema().nullable.shouldBeFalse()
    }

    "gets schema additionalProperties" {
        schema("additionalProperties: false").additionalProperties.shouldBeInstanceOf<Boolean>()
        schema("additionalProperties: {}").additionalProperties.shouldBeInstanceOf<Schema>()
    }

    "gets schema additionalProperties is true if missing" {
        schema().additionalProperties shouldBe true
    }
})

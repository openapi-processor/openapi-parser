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
import io.openapiparser.NoValueException

/**
 * @see [io.openapiparser.model.v3x.SchemaSpec]
 */
class SchemaSpec: StringSpec({

    // todo
    // properties  Property definitions MUST be a Schema Object and not a standard JSON Schema (inline or referenced).
    // additionalProperties - Value can be boolean or object. Inline or referenced schema MUST be of a Schema Object and not a standard JSON Schema. Consistent with JSON Schema, additionalProperties defaults to true.
    // allOf - Inline or referenced schema MUST be of a Schema Object and not a standard JSON Schema.
    // oneOf - Inline or referenced schema MUST be of a Schema Object and not a standard JSON Schema.
    // anyOf - Inline or referenced schema MUST be of a Schema Object and not a standard JSON Schema.
    // not - Inline or referenced schema MUST be of a Schema Object and not a standard JSON Schema.
    // items - Value MUST be an object and not an array. Inline or referenced schema MUST be of a Schema Object and not a standard JSON Schema. items MUST be present if the type is array.
    // readOnly
    // writeOnly

    // descriminator
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

})

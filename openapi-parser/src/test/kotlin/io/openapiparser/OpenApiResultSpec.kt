/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.openapiparser.schema.*
import io.openapiparser.model.v30.OpenApi as OpenApi30
import io.openapiparser.model.v31.OpenApi as OpenApi31
import io.openapiparser.validator.Validator
import io.openapiparser.OpenApiResult30.OPENAPI_SCHEMA as OPENAPI_SCHEMA_30
import io.openapiparser.OpenApiResult31.OPENAPI_SCHEMA as OPENAPI_SCHEMA_31

class OpenApiResultSpec: StringSpec({

    "should have version 30" {
        val ctx = mockk<Context>()
        val result = OpenApiResult30(ctx)

        result.version shouldBe OpenApiResult.Version.V30
    }

    "should return api 30" {
        val ctx = mockk<Context>()
        every { ctx.baseNode } returns Node("$", emptyMap())

        val result = OpenApiResult30(ctx)

        result.getModel(OpenApi30::class.java).shouldBeInstanceOf<OpenApi30>()
    }

    "should throw if model type does not match api version 30" {
        val ctx = mockk<Context>()
        every { ctx.baseNode } returns Node("$", emptyMap())

        val result = OpenApiResult30(ctx)

        shouldThrow<IllegalArgumentException> {
            result.getModel(OpenApi31::class.java)
        }
    }

    "should have version 31" {
        val ctx = mockk<Context>()
        every { ctx.baseNode } returns Node("$", emptyMap())

        val result = OpenApiResult31(ctx)

        result.version shouldBe OpenApiResult.Version.V31
    }

    "should throw if model type does not match api version 31" {
        val ctx = mockk<Context>()
        every { ctx.baseNode } returns Node("$", emptyMap())

        val result = OpenApiResult31(ctx)

        shouldThrow<IllegalArgumentException> {
            result.getModel(OpenApi30::class.java)
        }
    }

    "should return api 31" {
        val ctx = mockk<Context>()
        every { ctx.baseNode } returns Node("$", emptyMap())

        val result = OpenApiResult31(ctx)

        result.getModel(OpenApi31::class.java).shouldBeInstanceOf<OpenApi31>()
    }

    "should validate api 30" {
        val document = emptyMap<String, Any>()
        val schema = JsonSchemaBoolean(true)

        val ctx = mockk<Context>()
        every { ctx.rawObject } returns document

        val store = mockk<SchemaStore>()
        every { store.addSchema(OPENAPI_SCHEMA_30) } returns schema

        val validator = mockk<Validator>()
        every { validator.validate(any(), any()) } returns emptyList()

        // when
        val result = OpenApiResult30(ctx)
        result.validate(validator, store)

        // then
        verify { validator.validate(schema, document) }
    }

    "should validate api 31" {
        val document = emptyMap<String, Any>()
        val schema = JsonSchemaBoolean(true)

        val ctx = mockk<Context>()
        every { ctx.rawObject } returns document

        val store = mockk<SchemaStore>()
        every { store.addSchema(OPENAPI_SCHEMA_31) } returns schema

        val validator = mockk<Validator>()
        every { validator.validate(any(), any()) } returns emptyList()

        // when
        val result = OpenApiResult31(ctx)
        result.validate(validator, store)

        // then
        verify { validator.validate(schema, document) }
    }
})

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
import io.openapiparser.OpenApiSchemas.*
import io.openapiparser.schema.*
import io.openapiparser.validator.Validator
import io.openapiparser.validator.steps.ValidationStep
import io.openapiparser.model.v30.OpenApi as OpenApi30
import io.openapiparser.model.v31.OpenApi as OpenApi31

import java.net.URI

class OpenApiResultSpec: StringSpec({

    "should have version 30" {
        val ctx = mockk<Context>()
        val result = OpenApiResult30(ctx, Bucket.empty())

        result.version shouldBe OpenApiResult.Version.V30
    }

    "should return api 30" {
        val ctx = mockk<Context>()
        val result = OpenApiResult30(ctx, Bucket.empty())

        result.getModel(OpenApi30::class.java).shouldBeInstanceOf<OpenApi30>()
    }

    "should throw if model type does not match api version 30" {
        val ctx = mockk<Context>()
        val result = OpenApiResult30(ctx, Bucket.empty())

        shouldThrow<IllegalArgumentException> {
            result.getModel(OpenApi31::class.java)
        }
    }

    "should have version 31" {
        val ctx = mockk<Context>()
        val result = OpenApiResult31(ctx, Bucket.empty())

        result.version shouldBe OpenApiResult.Version.V31
    }

    "should throw if model type does not match api version 31" {
        val ctx = mockk<Context>()
        val result = OpenApiResult31(ctx, Bucket.empty())

        shouldThrow<IllegalArgumentException> {
            result.getModel(OpenApi30::class.java)
        }
    }

    "should return api 31" {
        val ctx = mockk<Context>()
        val result = OpenApiResult31(ctx, Bucket.empty())

        result.getModel(OpenApi31::class.java).shouldBeInstanceOf<OpenApi31>()
    }

    "should validate api 30" {
        val sctx = mockk<JsonSchemaContext>()
        val document = emptyMap<String, Any>()
        val bucket = Bucket(URI.create(""), "/unused", document)
        val schema = JsonSchemaBoolean(true, sctx)

        val jic = mockk<JsonInstanceContext>()
        val ctx = mockk<Context>()
        every { ctx.instanceContext } returns jic

        val store = mockk<SchemaStore>()
        every { store.addSchema(OPENAPI_SCHEMA_30_ID, OPENAPI_SCHEMA_30) } returns schema

        val validator = mockk<Validator>()
        val step = mockk<ValidationStep>()
        every { step.messages } returns emptyList()
        every { validator.validate(any(), any()) } returns step

        // when
        val result = OpenApiResult30(ctx, bucket)
        result.validate(validator, store)

        // then
        verify { validator.validate(schema, any()) }
    }

    "should validate api 31".config(enabled = false) {
        val sctx = mockk<JsonSchemaContext>()
        val document = emptyMap<String, Any>()
        val bucket = Bucket(URI.create(""), "/unused", document)
        val schema = JsonSchemaBoolean(true, sctx)


        val jic = mockk<JsonInstanceContext>()
        val ctx = mockk<Context>()
        every { ctx.instanceContext } returns jic

        val store = mockk<SchemaStore>()
        every { store.addSchema(OPENAPI_SCHEMA_31_ID, OPENAPI_SCHEMA_31) } returns schema

        val validator = mockk<Validator>()
        val step = mockk<ValidationStep>()
        every { validator.validate(any(), any()) } returns step

        // when
        val result = OpenApiResult31(ctx, bucket)
        result.validate(validator, store)

        // then
        verify { validator.validate(schema, any()) }
    }
})

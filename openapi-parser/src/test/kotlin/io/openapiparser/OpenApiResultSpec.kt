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
import io.openapiparser.OpenApiSchemas.OPENAPI_SCHEMA_30_ID
import io.openapiparser.OpenApiSchemas.OPENAPI_SCHEMA_31_ID
import io.openapiparser.support.ApiBuilder
import io.openapiprocessor.jackson.JacksonConverter
import io.openapiprocessor.jsonschema.reader.UriReader
import io.openapiprocessor.jsonschema.schema.*
import io.openapiprocessor.jsonschema.support.Uris
import io.openapiprocessor.jsonschema.support.Uris.emptyUri
import io.openapiprocessor.jsonschema.validator.Validator
import io.openapiprocessor.jsonschema.validator.steps.ValidationStep
import java.net.URI
import io.openapiparser.model.v30.OpenApi as OpenApi30
import io.openapiparser.model.v31.OpenApi as OpenApi31

class OpenApiResultSpec: StringSpec({

    "should have version 30" {
        val result = OpenApiResult30(mockk<Context>(), Bucket.empty(), DocumentStore())

        result.version shouldBe OpenApiResult.Version.V30
    }

    "should return api 30" {
        val result = OpenApiResult30(mockk<Context>(), Bucket.empty(), DocumentStore())

        result.getModel(OpenApi30::class.java).shouldBeInstanceOf<OpenApi30>()
    }

    "should throw if model type does not match api version 30" {
        val result = OpenApiResult30(mockk<Context>(), Bucket.empty(), DocumentStore())

        shouldThrow<IllegalArgumentException> {
            result.getModel(OpenApi31::class.java)
        }
    }

    "should have version 31" {
        val result = OpenApiResult31(mockk<Context>(), Bucket.empty(), DocumentStore())

        result.version shouldBe OpenApiResult.Version.V31
    }

    "should throw if model type does not match api version 31" {
        val result = OpenApiResult31(mockk<Context>(), Bucket.empty(), DocumentStore())

        shouldThrow<IllegalArgumentException> {
            result.getModel(OpenApi30::class.java)
        }
    }

    "should return api 31" {
        val result = OpenApiResult31(mockk<Context>(), Bucket.empty(), DocumentStore())

        result.getModel(OpenApi31::class.java).shouldBeInstanceOf<OpenApi31>()
    }

    "should validate api 30" {
        val parser = ApiBuilder()
            .withApi("""
                openapi: 3.0.3
                bad: property
            """.trimIndent())
            .buildParser()

        val result = parser.parse(emptyUri())

        val schemaStore = SchemaStore(DocumentLoader(UriReader(), JacksonConverter ()))
        val validator = Validator()

        val valid = result.validate(validator, schemaStore)
        valid.shouldBe(false)
    }

    "should validate api 31" {
        val parser = ApiBuilder()
            .withApi("""
                openapi: 3.1.0
                bad: property
            """.trimIndent())
            .buildParser()

        val result = parser.parse(emptyUri())

        val schemaStore = SchemaStore(DocumentLoader(UriReader(), JacksonConverter ()))
        val validator = Validator()

        val valid = result.validate(validator, schemaStore)
        valid.shouldBe(false)
    }
})

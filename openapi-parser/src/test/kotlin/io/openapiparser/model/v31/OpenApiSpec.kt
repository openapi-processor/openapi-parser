/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v31

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

/**
 * @see [io.openapiparser.model.v3x.OpenApiSpec]
 */
class OpenApiSpec: StringSpec({

    "gets json schema dialect" {
        openapi("jsonSchemaDialect: https://schema.uri").jsonSchemaDialect.shouldNotBeNull()
    }

    "gets null json schema dialect if it missing" {
        openapi().jsonSchemaDialect.shouldBeNull()
    }

    "gets paths object" {
        openapi("paths: {}").paths.shouldNotBeNull()
    }

    "gets path object is null if it missing" {
        openapi().paths.shouldBeNull()
    }

    "gets webhooks objects" {
        val webhooks = openapi("""
            webhooks:
              /foo: {}
              /bar: {}
        """).webhooks

        webhooks.size shouldBe 2
        webhooks["/foo"].shouldNotBeNull()
        webhooks["/bar"].shouldNotBeNull()
    }

    "gets webhooks is empty if it is missing" {
        openapi().webhooks.shouldBeEmpty()
    }
})

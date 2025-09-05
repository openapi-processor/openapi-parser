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
import io.openapiparser.model.v31.openapi as openapi31
import io.openapiparser.model.v32.openapi as openapi32

/**
 * @see [io.openapiparser.model.v3x.OpenApiSpec]
 */
class OpenApiSpec: StringSpec({

    "gets json schema dialect" {
        openapi31("jsonSchemaDialect: https://schema.uri").jsonSchemaDialect.shouldNotBeNull()
        openapi32("jsonSchemaDialect: https://schema.uri").jsonSchemaDialect.shouldNotBeNull()
    }

    "gets null json schema dialect if it missing" {
        openapi31().jsonSchemaDialect.shouldBeNull()
        openapi32().jsonSchemaDialect.shouldBeNull()
    }

    "gets paths object" {
        openapi31("paths: {}").paths.shouldNotBeNull()
        openapi32("paths: {}").paths.shouldNotBeNull()
    }

    "gets path object is null if it missing" {
        openapi31().paths.shouldBeNull()
        openapi32().paths.shouldBeNull()
    }

    fun assertWebhooks (webhooks: Map<String, Any?>) {
        webhooks.size shouldBe 2
        webhooks["/foo"].shouldNotBeNull()
        webhooks["/bar"].shouldNotBeNull()
    }

    "gets webhooks objects" {
        val source = """
          webhooks:
            /foo: {}
            /bar: {}
        """

        assertWebhooks(openapi31(source).webhooks)
        assertWebhooks(openapi32(source).webhooks)
    }

    "gets webhooks is empty if it is missing" {
        openapi31().webhooks.shouldBeEmpty()
        openapi32().webhooks.shouldBeEmpty()
    }
})

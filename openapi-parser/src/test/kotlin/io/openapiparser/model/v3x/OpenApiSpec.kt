/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v3x

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.datatest.withData
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.openapiparser.converter.NoValueException
import io.openapiparser.model.v30.openapi as openapi30
import io.openapiparser.model.v31.openapi as openapi31

class OpenApiSpec: StringSpec({

    "gets openapi version" {
        openapi30("openapi: 3.0.3").openapi shouldBe "3.0.3"
        openapi31("openapi: 3.1.0").openapi shouldBe "3.1.0"
    }

    "gets openapi version throws if it is missing" {
        shouldThrow<NoValueException> { openapi30().openapi }
        shouldThrow<NoValueException> { openapi31().openapi }
    }

    "gets info object" {
        openapi30("info: {}").info.shouldNotBeNull()
        openapi31("info: {}").info.shouldNotBeNull()
    }

    "gets info object throws if it missing" {
        shouldThrow<NoValueException> { openapi30().info }
        shouldThrow<NoValueException> { openapi30().info }
    }

    "gets server objects" {
        withData(
            openapi30("servers: [{}, {}]").servers,
            openapi31("servers: [{}, {}]").servers
        ) { servers ->
            servers.shouldNotBeNull()
            servers.size shouldBe 2
        }
    }

    "gets empty server objects if it is missing" {
        openapi30().servers.shouldBeEmpty()
        openapi31().servers.shouldBeEmpty()
    }

    "gets components objects" {
        val source = """
            components:
              foo: {}
              bar: {}
        """

        withData(
            openapi30(source).components!!,
            openapi31(source).components!!
        ) { components ->
            components.shouldNotBeNull()
        }
    }

    "gets components is null if it missing" {
        openapi30().components.shouldBeNull()
        openapi31().components.shouldBeNull()
    }

    "gets security requirements objects" {
        withData(
            openapi30("security: [{}, {}]").security,
            openapi31("security: [{}, {}]").security
        ) { security ->
            security.shouldNotBeNull()
            security.size shouldBe 2
        }
    }

    "gets empty security requirements objects if it is missing" {
        openapi30().security.shouldBeEmpty()
        openapi31().security.shouldBeEmpty()
    }

    "gets tags array" {
        withData(
            openapi30("tags: [{}, {}]").tags,
            openapi31("tags: [{}, {}]").tags,
        ) { tags ->
            tags.shouldNotBeEmpty()
            tags.size shouldBe 2
        }
    }

    "gets empty tag objects if it is missing" {
        openapi30().tags.shouldBeEmpty()
        openapi31().tags.shouldBeEmpty()
    }

    "gets external docs object" {
        openapi30("externalDocs: {}").externalDocs.shouldNotBeNull()
        openapi31("externalDocs: {}").externalDocs.shouldNotBeNull()
    }

    "gets external docs object is null if it missing" {
        openapi30().externalDocs.shouldBeNull()
        openapi31().externalDocs.shouldBeNull()
    }

    include(testExtensions("Openapi30", ::openapi30) { it.extensions })
    include(testExtensions("Openapi31", ::openapi31) { it.extensions })
})

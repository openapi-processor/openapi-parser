/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v3x

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.openapiprocessor.jsonschema.converter.NoValueException
import io.openapiparser.model.v30.server as server30
import io.openapiparser.model.v31.server as server31
import io.openapiparser.model.v32.server as server32

class ServerSpec: StringSpec({

    "gets server url" {
        server30("url: https://a.url").url shouldBe "https://a.url"
        server31("url: https://a.url").url shouldBe "https://a.url"
        server32("url: https://a.url").url shouldBe "https://a.url"
    }

    "gets server url throws if it is missing" {
        shouldThrow<NoValueException> { server30().url }
        shouldThrow<NoValueException> { server31().url }
        shouldThrow<NoValueException> { server32().url }
    }

    "gets server description" {
        server30("description: a description").description shouldBe "a description"
        server31("description: a description").description shouldBe "a description"
        server32("description: a description").description shouldBe "a description"
    }

    "gets server description is null if missing" {
        server30().description.shouldBeNull()
        server31().description.shouldBeNull()
        server32().description.shouldBeNull()
    }

    "gets server variables object" {
        server30("variables: {}").variables.shouldNotBeNull()
        server31("variables: {}").variables.shouldNotBeNull()
        server32("variables: {}").variables.shouldNotBeNull()
    }

    "gets empty variables objects if it is missing" {
        server30().variables.shouldBeEmpty()
        server31().variables.shouldBeEmpty()
        server32().variables.shouldBeEmpty()
    }

    include(testExtensions("Server30", ::server30) { it.extensions })
    include(testExtensions("Server31", ::server31) { it.extensions })
    include(testExtensions("Server32", ::server32) { it.extensions })
})

/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v3x

import io.kotest.core.datatest.forAll
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.openapiparser.model.v30.components as components30
import io.openapiparser.model.v31.components as components31

class ComponentsSpec: StringSpec({

    "gets components schemas" {
        val source = """
          schemas:
            foo: {}
            bar: {}
        """

        forAll(
            components30(source).schemas,
            components31(source).schemas,
        ) { schemas ->
            schemas.size shouldBe 2
            schemas["foo"].shouldNotBeNull()
            schemas["bar"].shouldNotBeNull()
        }
    }

    "gets components schemas is empty if missing" {
        components30().schemas.shouldBeEmpty()
        components31().schemas.shouldBeEmpty()
    }

    "gets components responses" {
        val source = """
          responses:
            foo: {}
            bar: {}
        """

        forAll(
            components30(source).responses,
            components31(source).responses
        ) { responses ->
            responses.size shouldBe 2
            responses["foo"].shouldNotBeNull()
            responses["bar"].shouldNotBeNull()
        }
    }

    "gets components responses is empty if missing" {
        components30().responses.shouldBeEmpty()
        components31().responses.shouldBeEmpty()
    }

    "gets components parameters" {
        val source = """
          parameters:
            foo: {}
            bar: {}
        """

        forAll(
            components30(source).parameters,
            components31(source).parameters
        ) { parameters ->
            parameters.size shouldBe 2
            parameters["foo"].shouldNotBeNull()
            parameters["bar"].shouldNotBeNull()
        }
    }

    "gets components parameters is empty if missing" {
        components30().parameters.shouldBeEmpty()
        components31().parameters.shouldBeEmpty()
    }

    "gets components examples" {
        val source = """
          examples:
            foo: {}
            bar: {}
        """

        forAll(
            components30(source).examples,
            components31(source).examples
        ) { examples ->
            examples.size shouldBe 2
            examples["foo"].shouldNotBeNull()
            examples["bar"].shouldNotBeNull()
        }
    }

    "gets components examples is empty if missing" {
        components30().examples.shouldBeEmpty()
        components31().examples.shouldBeEmpty()
    }

    "gets components requestBodies" {
        val source = """
          requestBodies:
            foo: {}
            bar: {}
        """

        forAll(
            components30(source).requestBodies,
            components31(source).requestBodies
        ) { requestBodies ->
            requestBodies.size shouldBe 2
            requestBodies["foo"].shouldNotBeNull()
            requestBodies["bar"].shouldNotBeNull()
        }
    }

    "gets components requestBodies is empty if missing" {
        components30().requestBodies.shouldBeEmpty()
        components31().requestBodies.shouldBeEmpty()
    }

    "gets components headers" {
        val source = """
          headers:
            foo: {}
            bar: {}
        """

        forAll(
            components30(source).headers,
            components31(source).headers
        ) { headers ->
            headers.size shouldBe 2
            headers["foo"].shouldNotBeNull()
            headers["bar"].shouldNotBeNull()
        }
    }

    "gets components headers is empty if missing" {
        components30().headers.shouldBeEmpty()
        components31().headers.shouldBeEmpty()
    }

    "gets components securitySchemes" {
        val source = """
          securitySchemes:
            foo: {}
            bar: {}
        """

        forAll(
            components30(source).securitySchemes,
            components31(source).securitySchemes
        ) { securitySchemes ->
            securitySchemes.size shouldBe 2
            securitySchemes["foo"].shouldNotBeNull()
            securitySchemes["bar"].shouldNotBeNull()
        }
    }

    "gets components securitySchemes is empty if missing" {
        components30().securitySchemes.shouldBeEmpty()
        components31().securitySchemes.shouldBeEmpty()
    }

    "gets components links" {
        val source = """
          links:
            foo: {}
            bar: {}
        """

        forAll(
            components30(source).links,
            components31(source).links
        ) { links ->
            links.size shouldBe 2
            links["foo"].shouldNotBeNull()
            links["bar"].shouldNotBeNull()
        }
    }

    "gets components links is empty if missing" {
        components30().links.shouldBeEmpty()
        components31().links.shouldBeEmpty()
    }

    "gets components callbacks" {
        val source = """
          callbacks:
            foo: {}
            bar: {}
        """

        forAll(
            components30(source).callbacks,
            components31(source).callbacks
        ) { callbacks ->
            callbacks.size shouldBe 2
            callbacks["foo"].shouldNotBeNull()
            callbacks["bar"].shouldNotBeNull()
        }
    }

    "gets components callbacks is empty if missing" {
        components30().callbacks.shouldBeEmpty()
        components31().callbacks.shouldBeEmpty()
    }

    include(testExtensions("Components 30", ::components30) { it.extensions })
    include(testExtensions("Components 31", ::components31) { it.extensions })
})

/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v3x

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.openapiparser.model.v30.components as components30
import io.openapiparser.model.v31.components as components31

class ComponentsSpec: StringSpec({

    fun assertMap (items: Map<String, Any>) {
        items.size shouldBe 2
        items["foo"].shouldNotBeNull()
        items["bar"].shouldNotBeNull()
    }

    "gets components schemas" {
        val source = """
          schemas:
            foo: {}
            bar: {}
        """

        assertMap(components30(source).schemas)
        assertMap(components31(source).schemas)
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

        assertMap(components30(source).responses)
        assertMap(components31(source).responses)
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

        assertMap(components30(source).parameters)
        assertMap(components31(source).parameters)
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

        assertMap(components30(source).examples)
        assertMap(components31(source).examples)
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

        assertMap(components30(source).requestBodies)
        assertMap(components31(source).requestBodies)
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

        assertMap(components30(source).headers)
        assertMap(components31(source).headers)
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

        assertMap(components30(source).securitySchemes)
        assertMap(components31(source).securitySchemes)
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

        assertMap(components30(source).links)
        assertMap(components31(source).links)
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

        assertMap(components30(source).callbacks)
        assertMap(components31(source).callbacks)
    }

    "gets components callbacks is empty if missing" {
        components30().callbacks.shouldBeEmpty()
        components31().callbacks.shouldBeEmpty()
    }

    include(testExtensions("Components 30", ::components30) { it.extensions })
    include(testExtensions("Components 31", ::components31) { it.extensions })
})

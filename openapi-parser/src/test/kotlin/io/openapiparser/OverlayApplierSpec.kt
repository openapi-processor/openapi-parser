/*
 * Copyright 2025 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldNotMatchEach
import io.kotest.matchers.equals.shouldNotBeEqual
import io.kotest.matchers.maps.shouldNotHaveKey
import io.kotest.matchers.nulls.shouldNotBeNull
import io.openapiparser.support.ApiBuilder
import io.openapiparser.support.OverlayBuilder
import io.openapiprocessor.jsonschema.converter.NoValueException
import io.openapiprocessor.jsonschema.schema.Bucket
import io.openapiprocessor.jsonschema.schema.ReferenceRegistry
import io.openapiprocessor.jsonschema.schema.Scope
import java.net.URI

class OverlayApplierSpec : StringSpec({
    val emptyContext = Context(Scope.empty(), ReferenceRegistry())

    fun createOpenApi(): OpenApiResult {
        return ApiBuilder()
            .withApi("file:///openapi.yaml",
                $$"""
                openapi: "3.1.0"
                info:
                  version: 1.0.0
                  title: overlay
                paths:
                  /foo:
                    get:
                      parameters:
                        - in: query
                          name: p1
                          schema:
                            type: string
                        - in: query
                          name: p2
                          schema:
                            type: string
                      summary: foo
                      responses:
                        '200':
                          description: result
                          content:
                            application/json:
                              schema:
                                $ref: '#/components/schemas/Foo'
                
                components:
                  schemas:
                    Foo:
                      type: object
                      properties:
                        foo:
                          type: string
                        bar:
                          type: array
                          items:
                            type: string
            """.trimIndent())
            .buildParser()
            .parse(URI("file:///openapi.yaml"))
    }

    "throws on empty actions" {
        val openapi = emptyMap<String, Any>()
        val overlay = OverlayApplier(openapi)

        shouldThrow<NoValueException> {
            overlay.apply(OverlayResult10(emptyContext, Bucket.empty()))
        }
    }

    "remove from object" {
        val openapi = createOpenApi()
        val overlay = OverlayBuilder()
            .withOverlay(
                """
                overlay: 1.0.0
                info:
                  title: Remove
                  version: 1.0.0
                actions:
                  - description: remove property from object
                    target: '$.components.schemas.Foo.properties.foo'
                    remove: true
            """.trimIndent())
            .buildOverlay10()

        val overlaid = openapi.apply(overlay)

        val api = ApiBuilder()
            .withDocument(overlaid)
            .buildOpenApi31()

        val schema = api.components!!.schemas["Foo"]
        schema!!.properties shouldNotHaveKey "foo"
    }

    "remove from array" {
        val openapi = createOpenApi()
        val overlay = OverlayBuilder()
            .withOverlay("""
                overlay: 1.0.0
                info:
                  title: Remove
                  version: 1.0.0
                actions:
                  - description: remove item from array
                    target: '${'$'}.paths./foo.get.parameters[?(@.name == "p1")]'
                    remove: true
            """.trimIndent())
            .buildOverlay10()

        val overlaid = openapi.apply(overlay)

        val api = ApiBuilder()
            .withDocument(overlaid)
            .buildOpenApi31()

        val parameters = api.paths!!.getPathItem("/foo")!!.parameters
        parameters.shouldNotMatchEach({ it.name shouldNotBeEqual "p1" })
    }

    "update object in array" {
        val openapi = createOpenApi()
        val overlay = OverlayBuilder()
            .withOverlay("""
                overlay: 1.0.0
                info:
                  title: Remove
                  version: 1.0.0
                actions:
                  - description: change parameter name
                    target: ${'$'}.paths./foo.get.parameters[?(@.name == "p1")]
                    update:
                      name: renamed
            """.trimIndent())
            .buildOverlay10()

        val overlaid = openapi.apply(overlay)

        val api = ApiBuilder()
            .withDocument(overlaid)
            .buildOpenApi31()

        val parameters = api.paths!!.getPathItem("/foo")!!.get!!.parameters
        val names = parameters.map { it.name }
        names shouldContainExactly listOf("renamed", "p2")
    }

    "adds object to array" {
        val openapi = createOpenApi()
        val overlay = OverlayBuilder()
            .withOverlay("""
                overlay: 1.0.0
                info:
                  title: Remove
                  version: 1.0.0
                actions:
                  - description: add parameter to parameter array
                    target: ${'$'}.paths['/foo'].get.parameters
                    update:
                      in: query
                      name: p3
                      schema:
                      type: string
            """.trimIndent())
            .buildOverlay10()

        val overlaid = openapi.apply(overlay)

        val api = ApiBuilder()
            .withDocument(overlaid)
            .buildOpenApi31()

        val parameters = api.paths!!.getPathItem("/foo")!!.get!!.parameters
        parameters shouldHaveSize 3
        parameters.find { it.name == "p3" }.shouldNotBeNull()
    }
})

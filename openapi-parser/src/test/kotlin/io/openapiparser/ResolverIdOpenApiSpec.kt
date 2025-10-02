/*
 * Copyright 2025 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.openapiprocessor.interfaces.Converter
import io.openapiprocessor.interfaces.Reader
import io.openapiprocessor.jsonschema.reader.UriReader
import io.openapiprocessor.jsonschema.schema.*
import io.openapiprocessor.jsonschema.support.Uris
import io.openapiprocessor.snakeyaml.SnakeYamlConverter
import java.net.URI

class ResolverIdOpenApiSpec : StringSpec({

    val scope = Scope(
        Uris.emptyUri(),
        Uris.emptyUri(),
        SchemaVersion.Draft202012
    )

    $$"OpenAPI detects $id in /components/schemas/.." {
        val documents = DocumentStore()
        val reader: Reader = UriReader()
        val converter: Converter = SnakeYamlConverter()
        val loader = DocumentLoader(reader, converter)

        val registry = ReferenceRegistry()
        val context = ResolverContext(
            documents,
            loader,
            registry
        )
        val resolver = ResolverId(context, OpenApiSchemaDetector())

        val bucket = Bucket(
            scope, mapOf(
                "components" to mapOf(
                    "schemas" to mapOf(
                        "Foo" to mapOf($$"$id" to "https://foo"),
                        "Bar" to mapOf($$"$id" to "https://bar")
                    )
                )
            )
        )

        resolver.resolve(bucket)

        documents.contains(URI("https://foo")).shouldBeTrue()
        documents.contains(URI("https://bar")).shouldBeTrue()
        registry.hasReference(URI("https://foo")).shouldBeTrue()
        registry.hasReference(URI("https://bar")).shouldBeTrue()
    }

    $$"OpenAPI detects $id in /paths/<path>/parameters/<index>/schema array" {
        val documents = DocumentStore()
        val reader: Reader = UriReader()
        val converter: Converter = SnakeYamlConverter()
        val loader = DocumentLoader(reader, converter)

        val registry = ReferenceRegistry()
        val context = ResolverContext(
            documents,
            loader,
            registry
        )
        val resolver = ResolverId(context, OpenApiSchemaDetector())

        val bucket = Bucket(
            scope, mapOf(
                "paths" to mapOf(
                    "foo" to mapOf(
                        "parameters" to listOf(
                            mapOf(
                                "name" to "foo",
                                "schema" to mapOf(
                                    $$"$id" to "https://foo"
                                )
                            ),
                            mapOf(
                                "name" to "bar",
                                "schema" to mapOf(
                                    $$"$id" to "https://bar"
                                )
                            )
                        )
                    )
                )
            )
        )

        resolver.resolve(bucket)

        documents.contains(URI("https://foo")).shouldBeTrue()
        documents.contains(URI("https://bar")).shouldBeTrue()
        registry.hasReference(URI("https://foo")).shouldBeTrue()
        registry.hasReference(URI("https://bar")).shouldBeTrue()
    }

    $$"OpenAPI detects $id in /paths/<path>/<method>/parameters/<index>/schema array" {
        val documents = DocumentStore()
        val reader: Reader = UriReader()
        val converter: Converter = SnakeYamlConverter()
        val loader = DocumentLoader(reader, converter)

        val registry = ReferenceRegistry()
        val context = ResolverContext(
            documents,
            loader,
            registry
        )
        val resolver = ResolverId(context, OpenApiSchemaDetector())

        val bucket = Bucket(
            scope, mapOf(
                "paths" to mapOf(
                    "foo" to mapOf(
                        "get" to mapOf(
                            "parameters" to listOf(
                                mapOf(
                                    "name" to "foo",
                                    "schema" to mapOf(
                                        $$"$id" to "https://foo"
                                    )
                                ),
                                mapOf(
                                    "name" to "bar",
                                    "schema" to mapOf(
                                        $$"$id" to "https://bar"
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )

        resolver.resolve(bucket)

        documents.contains(URI("https://foo")).shouldBeTrue()
        documents.contains(URI("https://bar")).shouldBeTrue()
        registry.hasReference(URI("https://foo")).shouldBeTrue()
        registry.hasReference(URI("https://bar")).shouldBeTrue()
    }

    $$"OpenAPI detects $id in /paths/<path>/requestBody/content/<mediatype>/schema map" {
        val documents = DocumentStore()
        val reader: Reader = UriReader()
        val converter: Converter = SnakeYamlConverter()
        val loader = DocumentLoader(reader, converter)

        val registry = ReferenceRegistry()
        val context = ResolverContext(
            documents,
            loader,
            registry
        )
        val resolver = ResolverId(context, OpenApiSchemaDetector())

        val bucket = Bucket(
            scope, mapOf(
                "paths" to mapOf(
                    "foo" to mapOf(
                        "get" to mapOf(
                            "requestBody" to mapOf(
                                "content" to mapOf(
                                    "foo" to mapOf(
                                        "schema" to mapOf(
                                            $$"$id" to "https://foo"
                                        )
                                    ),
                                    "bar" to mapOf(
                                        "schema" to mapOf(
                                            $$"$id" to "https://bar"
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )

        resolver.resolve(bucket)

        documents.contains(URI("https://foo")).shouldBeTrue()
        documents.contains(URI("https://bar")).shouldBeTrue()
        registry.hasReference(URI("https://foo")).shouldBeTrue()
        registry.hasReference(URI("https://bar")).shouldBeTrue()
    }

    $$"OpenAPI detects $id in /paths/<path>/responses/<status>/content/<mediatype>/schema map" {
        val documents = DocumentStore()
        val reader: Reader = UriReader()
        val converter: Converter = SnakeYamlConverter()
        val loader = DocumentLoader(reader, converter)

        val registry = ReferenceRegistry()
        val context = ResolverContext(
            documents,
            loader,
            registry
        )
        val resolver = ResolverId(context, OpenApiSchemaDetector())

        val bucket = Bucket(
            scope, mapOf(
                "paths" to mapOf(
                    "foo" to mapOf(
                        "get" to mapOf(
                            "responses" to mapOf(
                                "200" to mapOf(
                                    "content" to mapOf(
                                        "media type foo" to mapOf(
                                            "schema" to mapOf(
                                                $$"$id" to "https://foo"
                                            )
                                        ),
                                        "media type bar" to mapOf(
                                            "schema" to mapOf(
                                                $$"$id" to "https://bar"
                                            )
                                        )
                                    )
                                ),
                                "201" to mapOf(
                                    "content" to mapOf(
                                        "media type oof" to mapOf(
                                            "schema" to mapOf(
                                                $$"$id" to "https://oof"
                                            )
                                        ),
                                        "media type rab" to mapOf(
                                            "schema" to mapOf(
                                                $$"$id" to "https://rab"
                                            )
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )

        resolver.resolve(bucket)

        documents.contains(URI("https://foo")).shouldBeTrue()
        documents.contains(URI("https://bar")).shouldBeTrue()
        documents.contains(URI("https://oof")).shouldBeTrue()
        documents.contains(URI("https://rab")).shouldBeTrue()
        registry.hasReference(URI("https://foo")).shouldBeTrue()
        registry.hasReference(URI("https://bar")).shouldBeTrue()
        registry.hasReference(URI("https://oof")).shouldBeTrue()
        registry.hasReference(URI("https://rab")).shouldBeTrue()
    }

    $$"OpenAPI detects $id in /components/responses/.." {
        val documents = DocumentStore()
        val reader: Reader = UriReader()
        val converter: Converter = SnakeYamlConverter()
        val loader = DocumentLoader(reader, converter)

        val registry = ReferenceRegistry()
        val context = ResolverContext(
            documents,
            loader,
            registry
        )
        val resolver = ResolverId(context, OpenApiSchemaDetector())

        val bucket = Bucket(
            scope, mapOf(
                "components" to mapOf(
                    "responses" to mapOf(
                        "name" to mapOf(
                            "content" to mapOf(
                                "foo" to mapOf(
                                    "schema" to mapOf(
                                        $$"$id" to "https://foo"
                                    )
                                ),
                                "bar" to mapOf(
                                    "schema" to mapOf(
                                        $$"$id" to "https://bar"
                                    )
                                )
                            )
                        ),
                        "other name" to mapOf(
                            "content" to mapOf(
                                "media type oof" to mapOf(
                                    "schema" to mapOf(
                                        $$"$id" to "https://oof"
                                    )
                                ),
                                "media type rab" to mapOf(
                                    "schema" to mapOf(
                                        $$"$id" to "https://rab"
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )

        resolver.resolve(bucket)

        documents.contains(URI("https://foo")).shouldBeTrue()
        documents.contains(URI("https://bar")).shouldBeTrue()
        documents.contains(URI("https://oof")).shouldBeTrue()
        documents.contains(URI("https://rab")).shouldBeTrue()
        registry.hasReference(URI("https://foo")).shouldBeTrue()
        registry.hasReference(URI("https://bar")).shouldBeTrue()
        registry.hasReference(URI("https://oof")).shouldBeTrue()
        registry.hasReference(URI("https://rab")).shouldBeTrue()
    }

    $$"OpenAPI detects $id in /components/parameters/.." {
        val documents = DocumentStore()
        val reader: Reader = UriReader()
        val converter: Converter = SnakeYamlConverter()
        val loader = DocumentLoader(reader, converter)

        val registry = ReferenceRegistry()
        val context = ResolverContext(
            documents,
            loader,
            registry
        )
        val resolver = ResolverId(context, OpenApiSchemaDetector())

        val bucket = Bucket(
            scope, mapOf(
                "components" to mapOf(
                    "parameters" to mapOf(
                        "foo" to mapOf(
                            "name" to "foo",
                            "schema" to mapOf(
                                $$"$id" to "https://foo"
                            )
                        ),
                        "bar" to mapOf(
                            "name" to "bar",
                            "schema" to mapOf(
                                $$"$id" to "https://bar"
                            )
                        )
                    )
                )
            )
        )

        resolver.resolve(bucket)

        documents.contains(URI("https://foo")).shouldBeTrue()
        documents.contains(URI("https://bar")).shouldBeTrue()
        registry.hasReference(URI("https://foo")).shouldBeTrue()
        registry.hasReference(URI("https://bar")).shouldBeTrue()
    }

    $$"OpenAPI detects $id in /components/requestBodies/.." {
        val documents = DocumentStore()
        val reader: Reader = UriReader()
        val converter: Converter = SnakeYamlConverter()
        val loader = DocumentLoader(reader, converter)

        val registry = ReferenceRegistry()
        val context = ResolverContext(
            documents,
            loader,
            registry
        )
        val resolver = ResolverId(context, OpenApiSchemaDetector())

        val bucket = Bucket(
            scope, mapOf(
                "components" to mapOf(
                    "requestBodies" to mapOf(
                        "name" to mapOf(
                            "content" to mapOf(
                                "foo" to mapOf(
                                    "schema" to mapOf(
                                        $$"$id" to "https://foo"
                                    )
                                ),
                                "bar" to mapOf(
                                    "schema" to mapOf(
                                        $$"$id" to "https://bar"
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )

        resolver.resolve(bucket)

        documents.contains(URI("https://foo")).shouldBeTrue()
        documents.contains(URI("https://bar")).shouldBeTrue()
        registry.hasReference(URI("https://foo")).shouldBeTrue()
        registry.hasReference(URI("https://bar")).shouldBeTrue()
    }
})

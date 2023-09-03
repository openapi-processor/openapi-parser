/*
 * Copyright 2023 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.schema

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.openapiprocessor.interfaces.Converter
import io.openapiprocessor.interfaces.Reader
import io.openapiprocessor.jsonschema.reader.UriReader
import io.openapiprocessor.jsonschema.support.Uris
import io.openapiprocessor.snakeyaml.SnakeYamlConverter
import java.net.URI


class ResolverIdSpec : StringSpec({

    val scope = Scope(
        Uris.emptyUri(),
        Uris.emptyUri(),
        SchemaVersion.Draft4
    )

    "walks empty bucket" {
        val documents = DocumentStore()
        val reader: Reader = UriReader()
        val converter: Converter = SnakeYamlConverter()
        val loader = DocumentLoader(reader, converter)

        val context = ResolverContext(
            documents,
            loader,
            ReferenceRegistry()
        )
        val resolver = ResolverId(context)

        val bucket = Bucket(scope, emptyMap())

        resolver.resolve(bucket)

        documents.isEmpty.shouldBeTrue()
    }

    "walks schema keyword in bucket" {
        val documents = DocumentStore()
        val reader: Reader = UriReader()
        val converter: Converter = SnakeYamlConverter()
        val loader = DocumentLoader(reader, converter)

        val context = ResolverContext(
            documents,
            loader,
            ReferenceRegistry()
        )
        val resolver = ResolverId(context)

        val bucket = Bucket(
            scope, mapOf(
                "not" to mapOf(
                    "id" to "/id"
                )
            )
        )

        resolver.resolve(bucket)

        documents.contains(Uris.emptyUri())
        documents.contains(URI("/id")).shouldBeTrue()
    }

    "walks schema array keyword in bucket" {
        val documents = DocumentStore()
        val reader: Reader = UriReader()
        val converter: Converter = SnakeYamlConverter()
        val loader = DocumentLoader(reader, converter)

        val context = ResolverContext(
            documents,
            loader,
            ReferenceRegistry()
        )
        val resolver = ResolverId(context)

        val bucket = Bucket(
            scope, mapOf(
                "oneOf" to listOf(
                    mapOf("id" to "/oneA"),
                    mapOf("id" to "/oneB"),
                )
            )
        )

        resolver.resolve(bucket)

        documents.contains(URI(""))
        documents.contains(URI("/oneA")).shouldBeTrue()
        documents.contains(URI("/oneB")).shouldBeTrue()
    }

    "walks schema map keyword in bucket" {
        val documents = DocumentStore()
        val reader: Reader = UriReader()
        val converter: Converter = SnakeYamlConverter()
        val loader = DocumentLoader(reader, converter)

        val context = ResolverContext(
            documents,
            loader,
            ReferenceRegistry()
        )
        val resolver = ResolverId(context)

        val bucket = Bucket(
            scope, mapOf(
                "definitions" to mapOf(
                    "schemaA" to mapOf("id" to "/schemaA"),
                    "schemaB" to mapOf("id" to "/schemaB"),
                )
            )
        )

        resolver.resolve(bucket)

        documents.contains(URI(""))
        documents.contains(URI("/schemaA")).shouldBeTrue()
        documents.contains(URI("/schemaB")).shouldBeTrue()
    }
})

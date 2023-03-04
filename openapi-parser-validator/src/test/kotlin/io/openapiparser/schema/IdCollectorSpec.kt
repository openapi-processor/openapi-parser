/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.openapiparser.schema.UriSupport.emptyUri
import java.net.URI

class IdCollectorSpec : StringSpec({

    val scope = Scope(emptyUri(), emptyUri(), SchemaVersion.Draft4)

    "walks empty bucket" {
        val documents = DocumentStore()
        val walker = IdCollector(documents)

        val bucket = Bucket(scope, emptyMap())

        walker.walk(bucket)

        documents.contains(emptyUri()).shouldBeTrue()
    }

    "walks schema keyword in bucket" {
        val documents = DocumentStore()
        val walker = IdCollector(documents)

        val bucket = Bucket(scope, mapOf(
            "not" to mapOf(
                "id" to "/id"
            )
        ))

        walker.walk(bucket)

        documents.contains(emptyUri())
        documents.contains(URI("/id")).shouldBeTrue()
    }

    "walks schema array keyword in bucket" {
        val documents = DocumentStore()
        val walker = IdCollector(documents)

        val bucket = Bucket(scope, mapOf(
            "oneOf" to listOf(
                mapOf("id" to "/oneA"),
                mapOf("id" to "/oneB"),
            )))

        walker.walk(bucket)

        documents.contains(URI(""))
        documents.contains(URI("/oneA")).shouldBeTrue()
        documents.contains(URI("/oneB")).shouldBeTrue()
    }

    "walks schema map keyword in bucket" {
        val documents = DocumentStore()
        val walker = IdCollector(documents)

        val bucket = Bucket(scope, mapOf(
            "definitions" to mapOf(
                "schemaA" to mapOf("id" to "/schemaA"),
                "schemaB" to mapOf("id" to "/schemaB"),
            )))

        walker.walk(bucket)

        documents.contains(URI(""))
        documents.contains(URI("/schemaA")).shouldBeTrue()
        documents.contains(URI("/schemaB")).shouldBeTrue()
    }
})

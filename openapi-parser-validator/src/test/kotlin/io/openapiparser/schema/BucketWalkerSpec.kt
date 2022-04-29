/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeTrue
import java.net.URI

class BucketWalkerSpec : StringSpec({

    "walks empty bucket" {
        val version = SchemaVersion.Draft4
        val documents = DocumentStore()
        val walker = BucketWalker(version, documents)

        val bucket = Bucket(emptyMap())

        walker.walk(URI(""), bucket)

        documents.contains(URI("")).shouldBeTrue()
    }

    "walks schema keyword in bucket" {
        val version = SchemaVersion.Draft4
        val documents = DocumentStore()
        val walker = BucketWalker(version, documents)

        val bucket = Bucket(mapOf(
            "not" to mapOf(
                "id" to "/id"
            )
        ))

        walker.walk(URI(""), bucket)

        documents.contains(URI(""))
        documents.contains(URI("/id")).shouldBeTrue()
    }

    "walks schema array keyword in bucket" {
        val version = SchemaVersion.Draft4
        val documents = DocumentStore()
        val walker = BucketWalker(version, documents)

        val bucket = Bucket(mapOf(
            "oneOf" to listOf(
                mapOf("id" to "/oneA"),
                mapOf("id" to "/oneB"),
            )))

        walker.walk(URI(""), bucket)

        documents.contains(URI(""))
        documents.contains(URI("/oneA")).shouldBeTrue()
        documents.contains(URI("/oneB")).shouldBeTrue()
    }

    "walks schema map keyword in bucket" {
        val version = SchemaVersion.Draft4
        val documents = DocumentStore()
        val walker = BucketWalker(version, documents)

        val bucket = Bucket(mapOf(
            "definitions" to mapOf(
                "schemaA" to mapOf("id" to "/schemaA"),
                "schemaB" to mapOf("id" to "/schemaB"),
            )))

        walker.walk(URI(""), bucket)

        documents.contains(URI(""))
        documents.contains(URI("/schemaA")).shouldBeTrue()
        documents.contains(URI("/schemaB")).shouldBeTrue()
    }
})

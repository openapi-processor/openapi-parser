/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.schema

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.openapiprocessor.jsonschema.schema.Ref
import io.openapiprocessor.jsonschema.schema.SchemaVersion
import io.openapiprocessor.jsonschema.schema.Scope
import io.openapiprocessor.jsonschema.schema.UriSupport.emptyUri
import java.net.URI

class RefSpec: StringSpec({

    "absolute uri of empty ref is an empty uri" {
        val scope = Scope(
            emptyUri(),
            emptyUri(),
            SchemaVersion.Draft4
        )
        val ref = Ref(scope)

        ref.absoluteUri shouldBe emptyUri()
    }

    "absolute uri of empty ref is the base uri" {
        val scope = Scope(
            URI.create("https://foo/bar"),
            emptyUri(),
            SchemaVersion.Draft4
        )

        val emptyRef = Ref(scope)
        emptyRef.absoluteUri shouldBe URI.create("https://foo/bar")

        val hashRef = Ref(scope, "#")
        hashRef.absoluteUri shouldBe URI.create("https://foo/bar#")
    }

    "absolute uri of pointer ref is base uri with ref" {
        val scope = Scope(
            URI.create("https://foo/bar"),
            emptyUri(),
            SchemaVersion.Draft4
        )
        val ref = Ref(scope, "#/pointer")

        ref.absoluteUri shouldBe URI.create("https://foo/bar#/pointer")
    }

    "absolute uri of path/pointer ref is base uri with ref" {
        val scope = Scope(
            URI.create("https://foo/bar"),
            emptyUri(),
            SchemaVersion.Draft4
        )
        val ref = Ref(scope, "foo#/pointer")

        ref.absoluteUri shouldBe URI.create("https://foo/foo#/pointer")
    }

    "absolute uri of path/no pointer ref is base uri with ref" {
        val scope = Scope(
            URI.create("https://foo/bar"),
            emptyUri(),
            SchemaVersion.Draft4
        )
        val ref = Ref(scope, "foo#no-pointer")

        ref.absoluteUri shouldBe URI.create("https://foo/foo#no-pointer")
    }

    "document uri of empty ref with empty base uri is absolute ref without fragment" {
        val scope = Scope(
            emptyUri(),
            emptyUri(),
            SchemaVersion.Draft4
        )

        val emptyRef = Ref(scope)
        emptyRef.documentUri shouldBe emptyUri()

        val hashRef = Ref(scope, "#")
        hashRef.documentUri shouldBe emptyUri()
    }

    "document uri of empty ref with base uri is absolute ref without fragment" {
        val scope = Scope(
            URI.create("https://foo/bar.json"),
            emptyUri(),
            SchemaVersion.Draft4
        )

        val emptyRef = Ref(scope)
        emptyRef.documentUri shouldBe URI.create("https://foo/bar.json")

        val hashRef = Ref(scope, "#")
        hashRef.documentUri shouldBe URI.create("https://foo/bar.json")
    }

    "document uri of path/pointer ref with base uri is absolute ref without fragment" {
        val scope = Scope(
            URI.create("https://foo/bar"),
            emptyUri(),
            SchemaVersion.Draft4
        )
        val ref = Ref(scope, "foo#/pointer")

        ref.documentUri shouldBe URI.create("https://foo/foo")
    }

    "document uri of path/no pointer ref with base uri is absolute ref without fragment" {
        val scope = Scope(
            URI.create("https://foo/bar"),
            emptyUri(),
            SchemaVersion.Draft4
        )
        val ref = Ref(scope, "foo#no-pointer")

        ref.documentUri shouldBe URI.create("https://foo/foo")
    }
})

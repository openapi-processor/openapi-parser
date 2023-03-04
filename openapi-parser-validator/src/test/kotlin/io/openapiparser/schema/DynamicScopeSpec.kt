/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.kotest.matchers.types.shouldNotBeSameInstanceAs
import io.openapiparser.schema.UriSupport.*
import java.net.URI

class DynamicScopeSpec : StringSpec({

    fun createCtx(id: String?): JsonSchemaContext {
        val uri = if (id == null) {
            emptyUri()
        } else {
            URI.create(id)
        }

        return JsonSchemaContext(
            Scope(emptyUri(), uri, SchemaVersion.Draft201909),
            ReferenceRegistry()
        )
    }

    fun createSchema (id: String? = null, recursiveAnchor: Boolean? = null): JsonSchema {
        val map = mutableMapOf<String, Any>()
        if (id != null) {
            map["\$id"] = id
        }

        if (recursiveAnchor != null) {
            map["\$recursiveAnchor"] = recursiveAnchor
        }

        return JsonSchemaObject(map, createCtx(id))
    }

    val rootCtx = createCtx("")
    val rootSchema = JsonSchemaObject(mapOf<String, Any>(), rootCtx)
    val dynamicScope = DynamicScope(rootSchema)

    "create scope from schema without id" {
        val found = dynamicScope.findScope(URI.create("#"))
        found.shouldBe(rootCtx.scope.baseUri)
    }

    "adding schema without id should not change dynamic scope" {
        val schema = createSchema()

        val scope = dynamicScope.add(schema)

        dynamicScope.shouldBeSameInstanceAs(scope)
    }

    "adding schema with id should create new dynamic scope" {
        val schema = createSchema("https://localhost/foo")

        val scope = dynamicScope.add(schema)

        scope.shouldNotBeSameInstanceAs(dynamicScope)
    }

    "if current id has no dynamic anchor use current id" {
        var currentScope = dynamicScope

        val schema = createSchema("https://localhost/bar")
        currentScope = currentScope.add(schema)

        val scope = currentScope.findScope(URI.create("#"))

        scope.shouldBe(URI.create("https://localhost/bar"))
    }

    "if current id has dynamic anchor move up to last anchor" {
        var currentScope = dynamicScope

        val schemaB = createSchema("https://localhost/last", true)
        currentScope = currentScope.add(schemaB)

        val schemaA = createSchema("https://localhost/skip", true)
        currentScope = currentScope.add(schemaA)

        currentScope = currentScope.add(createSchema())
        currentScope = currentScope.add(createSchema())

        val scope = currentScope.findScope(URI.create("#"))

        scope.shouldBe(URI.create("https://localhost/last"))
    }

    "if current id has dynamic anchor move up to last anchor in a row" {
        var currentScope = dynamicScope

        val schemaA = createSchema("https://localhost/ignore", true)
        currentScope = currentScope.add(schemaA)

        val schemaB = createSchema("https://localhost/stop")
        currentScope = currentScope.add(schemaB)

        val schemaC = createSchema("https://localhost/last", true)
        currentScope = currentScope.add(schemaC)

        val schemaD = createSchema("https://localhost/skip", true)
        currentScope = currentScope.add(schemaD)

        currentScope = currentScope.add(createSchema())
        currentScope = currentScope.add(createSchema())

        val scope = currentScope.findScope(URI.create("#"))

        scope.shouldBe(URI.create("https://localhost/last"))
    }
})

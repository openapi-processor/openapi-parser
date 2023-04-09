/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.kotest.matchers.types.shouldNotBeSameInstanceAs
import io.openapiparser.schema.UriSupport.*
import java.net.URI

class DynamicScopeSpec : StringSpec({
    isolationMode = IsolationMode.InstancePerTest

    val registry = ReferenceRegistry()

    fun createSchema (id: String? = null, recursiveAnchor: Boolean? = null): JsonSchema {
        val uri = if(id != null) createUri(id) else emptyUri()

        val map = mutableMapOf<String, Any>()
        if (id != null) {
            map["\$id"] = id
        }

        val scope = Scope.createScope(uri, map, SchemaVersion.Draft201909)

        return if (recursiveAnchor == null) {
            registry.addReference(Ref(scope), scope, map)
            JsonSchemaObject(map, JsonSchemaContext(scope, registry))
        } else {
            map["\$recursiveAnchor"] = recursiveAnchor
            registry.addDynamicReference(Ref(scope, "#"), scope, map)
            JsonSchemaObject(map, JsonSchemaContext(scope, registry))
        }
    }

    val rootSchema = createSchema()
    val dynamicScope = DynamicScope(rootSchema)

    "create scope from schema without id" {
        val found = dynamicScope.findScope(URI.create("#"))
        found.shouldBe(rootSchema.context.scope.baseUri)
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

    "if current id has dynamic anchor move up to outermost scope" {
        var currentScope = dynamicScope

        currentScope = currentScope.add(createSchema("https://localhost/last", true))
        currentScope = currentScope.add(createSchema("https://localhost/skip", true))
        currentScope = currentScope.add(createSchema("https://localhost/current", true))

        val scope = currentScope.findScope(URI.create("#"))
        scope.shouldBe(URI.create("https://localhost/last"))
    }

    "if current id has dynamic anchor move up to outermost scope and ignore scopes without anchor" {
        var currentScope = dynamicScope

        currentScope = currentScope.add(createSchema("https://localhost/other", null))
        currentScope = currentScope.add(createSchema("https://localhost/last", true))
        currentScope = currentScope.add(createSchema("https://localhost/skip0", null))
        currentScope = currentScope.add(createSchema("https://localhost/skip1", true))
        currentScope = currentScope.add(createSchema("https://localhost/skip2", null))
        currentScope = currentScope.add(createSchema("https://localhost/current", true))

        val scope = currentScope.findScope(URI.create("#"))
        scope.shouldBe(URI.create("https://localhost/last"))
    }

//    "" {
//
//    }
})

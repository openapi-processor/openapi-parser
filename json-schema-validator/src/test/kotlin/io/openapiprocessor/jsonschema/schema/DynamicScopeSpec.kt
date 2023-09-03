/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.schema

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.kotest.matchers.types.shouldNotBeSameInstanceAs
import io.openapiprocessor.jsonschema.support.Uris.*
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
            JsonSchemaObject(
                map,
                JsonSchemaContext(scope, registry)
            )
        } else {
            map["\$recursiveAnchor"] = recursiveAnchor
            registry.addDynamicReference(Ref(scope, "#"), scope, map)
            JsonSchemaObject(
                map,
                JsonSchemaContext(scope, registry)
            )
        }
    }

    fun createSchema2 (id: String? = null, dynamicAnchor: String? = null): JsonSchema {
        val uri = if(id != null) createUri(id) else emptyUri()

        val map = mutableMapOf<String, Any>()
        if (id != null) {
            map["\$id"] = id
        }

        val scope = Scope.createScope(uri, map, SchemaVersion.Draft202012)

        return if (dynamicAnchor == null) {
            registry.addReference(Ref(scope), scope, map)
            JsonSchemaObject(
                map,
                JsonSchemaContext(scope, registry)
            )
        } else {
            map["\$dynamicAnchor"] = dynamicAnchor
            registry.addDynamicReference(
                Ref(
                    scope,
                    dynamicAnchor
                ), scope, map)
            JsonSchemaObject(
                map,
                JsonSchemaContext(scope, registry)
            )
        }
    }

    "create scope from schema without id" {
        val dynamicScope = DynamicScope(createSchema())

        val found = dynamicScope.findScope(URI.create("#"))
        found.shouldBeNull()
    }

    "adding schema without id should not change dynamic scope" {
        val dynamicScope = DynamicScope(createSchema())

        val schema = createSchema()
        val scope = dynamicScope.add(schema)

        dynamicScope.shouldBeSameInstanceAs(scope)
    }

    "adding schema with id should create new dynamic scope" {
        val dynamicScope = DynamicScope(createSchema())
        val scope = dynamicScope.add(createSchema("https://localhost/foo"))

        scope.shouldNotBeSameInstanceAs(dynamicScope)
    }

    "if current id has no dynamic anchor use current id" {
        var dynamicScope = DynamicScope(createSchema())
        dynamicScope = dynamicScope.add(createSchema("https://localhost/bar"))

        val scope = dynamicScope.findScope(URI.create("#"))
        scope.shouldBeNull()
    }

    "if current id has dynamic anchor move up to outermost scope" {
        var dynamicScope = DynamicScope(createSchema())
        dynamicScope = dynamicScope.add(createSchema("https://localhost/last", true))
        dynamicScope = dynamicScope.add(createSchema("https://localhost/skip", true))
        dynamicScope = dynamicScope.add(createSchema("https://localhost/current", true))

        val scope = dynamicScope.findScope(URI.create("#"))
        scope.shouldBe(URI.create("https://localhost/last"))
    }

    "if current id has dynamic anchor move up to outermost scope and ignore scopes without anchor" {
        var dynamicScope = DynamicScope(createSchema())
        dynamicScope = dynamicScope.add(createSchema("https://localhost/other", null))
        dynamicScope = dynamicScope.add(createSchema("https://localhost/last", true))
        dynamicScope = dynamicScope.add(createSchema("https://localhost/skip0", null))
        dynamicScope = dynamicScope.add(createSchema("https://localhost/skip1", true))
        dynamicScope = dynamicScope.add(createSchema("https://localhost/skip2", null))
        dynamicScope = dynamicScope.add(createSchema("https://localhost/current", true))

        val scope = dynamicScope.findScope(URI.create("#"))
        scope.shouldBe(URI.create("https://localhost/last"))
    }
})

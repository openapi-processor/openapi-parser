/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;
import java.util.Map;

import static io.openapiparser.schema.ScopeSupport.updateScope;

public class JsonSchemaContext {
    private final SchemaVersion version;
    private final URI scope;
    private final ReferenceRegistry references;

    public JsonSchemaContext (URI scope, ReferenceRegistry references, SchemaVersion version) {
        this.version = version;
        this.scope = scope;
        this.references = references;
    }

    public SchemaVersion getVersion () {
        return version;
    }

    public URI getScope () {
        return scope;
    }

    public Reference getReference (URI ref) {
        URI resolved = scope.resolve (ref);
        return references.getRef (resolved);
    }

    public Reference getReference (URI ref, URI dynamicScope) {
        URI resolved = dynamicScope.resolve (ref);
        return references.getRef (resolved);
    }

    public JsonSchemaContext withScope (@Nullable URI targetScope) {
        // todo possible??
        if (targetScope == null) {
            return this;
        }
        return new JsonSchemaContext (scope.resolve (targetScope), references, version);
    }

    public JsonSchemaContext withId (Map<String, Object> properties) {
        return new JsonSchemaContext (
            updateScope (properties, scope, version.getIdProvider ()),
            references,
            version);
    }
}

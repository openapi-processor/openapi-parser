/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;
import java.util.Map;

public class JsonInstanceContext {
    private final SchemaVersion version;
    private final URI scope;
    private final ReferenceRegistry references;

    public JsonInstanceContext (URI scope, ReferenceRegistry references) {
        this(scope, references, SchemaVersion.Default);
    }

    public JsonInstanceContext (URI scope, ReferenceRegistry references, SchemaVersion version) {
        this.scope = scope;
        this.references = references;
        this.version = version;
    }

    public URI getScope () {
        return scope;
    }

    public boolean hasReference (URI ref) {
        URI resolved = scope.resolve (ref);
        return references.hasRef (resolved);
    }

    public Reference getReference (URI ref) {
        URI resolved = scope.resolve (ref);
        return references.getRef (resolved);
    }
    public URI getReferenceKey (URI ref) {
        return scope.resolve (ref);
    }

    public JsonInstanceContext withScope (@Nullable URI targetScope) {
        // todo possible??
        if (targetScope == null) {
            return this;
        }
        return new JsonInstanceContext (scope.resolve (targetScope), references);
    }

    public JsonInstanceContext withId (@Nullable Map<String, Object> properties) {
        if (properties == null)
            return this;

        String id = version.getIdProvider ().getId (properties);
        if (id == null) {
            return this;
        }

        return withId (id);
    }

    public JsonInstanceContext withId (@Nullable String id) {
        if (id == null) {
            return this;
        }
        return new JsonInstanceContext (scope.resolve (id), references);
    }

    public boolean isRef(URI ref) {
        if (!hasReference (ref))
            return false;

        Reference reference = getReference (ref);
        return reference != null;
    }
}

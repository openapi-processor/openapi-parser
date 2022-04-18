/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;
import java.util.Map;

public class JsonSchemaContext {
    private final URI scope;
    private final ReferenceRegistry references;
    private final IdProvider idProvider;

    public JsonSchemaContext (URI scope, ReferenceRegistry references) {
        this.scope = scope;
        this.references = references;
        this.idProvider = new IdProvider ();
    }

    public URI getScope () {
        return scope;
    }

    public Reference getReference (URI ref) {
        URI resolved = scope.resolve (ref);
        return references.getRef (resolved);
    }

    @Deprecated
    public String getId (Map<String, Object> properties) {
        return idProvider.getId (properties);
    }

    @Deprecated
    public JsonSchemaContext withSource (URI source) {
        if (scope.equals (source)) {
            return this;
        }
        return new JsonSchemaContext (source, references);
    }

    public JsonSchemaContext withScope (@Nullable URI targetScope) {
        // todo possible??
        if (targetScope == null) {
            return this;
        }
        return new JsonSchemaContext (scope.resolve (targetScope), references);
    }

    public JsonSchemaContext withId (Map<String, Object> properties) {
        String id = idProvider.getId (properties);
        if (id == null) {
            return this;
        }

        return withId (id);
    }

    public JsonSchemaContext withId (@Nullable String id) {
        if (id == null) {
            return this;
        }
        return new JsonSchemaContext (scope.resolve (id), references);
    }
}

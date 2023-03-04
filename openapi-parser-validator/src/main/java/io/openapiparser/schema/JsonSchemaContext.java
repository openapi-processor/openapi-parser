/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import java.net.URI;
import java.util.Map;

public class JsonSchemaContext {
    private final Scope scope;
    private final ReferenceRegistry references;

    public JsonSchemaContext (Scope scope, ReferenceRegistry references) {
        this.scope = scope;
        this.references = references;
    }

    public SchemaVersion getVersion () {
        return scope.getVersion ();
    }

    public Scope getScope () {
        return scope;
    }

    public Reference getReference (URI ref) {
        if (ref.isAbsolute ()) {
            return references.getReference (ref);
        }

        // is id reference?  is absolute match?
        URI refId = scope.getBaseUri ().resolve (ref);
        if (references.hasReference (refId)) {
            return references.getReference (refId);
        }

        // is local reference..
        URI refLocal = scope.getBaseUri ().resolve (ref);
        return references.getReference (refLocal);
    }

    public Reference getReference (URI ref, URI dynamicScope) {
        URI resolved = dynamicScope.resolve (ref);
        return references.getReference (resolved);
    }

    public JsonSchemaContext withScope (Scope targetScope) {
        return new JsonSchemaContext (targetScope, references);
    }

    public JsonSchemaContext withId (Map<String, Object> properties) {
        return new JsonSchemaContext (scope.move (properties), references);
    }
}

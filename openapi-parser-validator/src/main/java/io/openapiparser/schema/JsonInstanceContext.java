/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;
import java.util.Map;

public class JsonInstanceContext {

    private final Scope scope;
    private final ReferenceRegistry references;

    public JsonInstanceContext (Scope scope, ReferenceRegistry references) {
        this.scope = scope;
        this.references = references;
    }

    public Scope getScope () {
        return scope;
    }

    public Reference getReference (URI ref) {
        if (ref.isAbsolute ()) {
            return references.getReference (ref);
        }

        // is id reference?
        URI refId = scope.getBaseUri ().resolve (ref);
        if (references.hasReference (refId)) {
            return references.getReference (refId);
        }

        // is local reference..
        URI refLocal = scope.getBaseUri ().resolve (ref);
        return references.getReference (refLocal);
    }

//    public JsonInstanceContext withScope (Scope targetScope) {
//        return new JsonInstanceContext (targetScope, references);
//    }

    // todo cf warning
//    public JsonInstanceContext withId (@Nullable Map<String, Object> properties) {
//        return new JsonInstanceContext (scope.move (properties), references);
//    }
}

/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;
import java.util.Map;

public class JsonInstanceContext {
    private final URI scope;
    private final ReferenceRegistry references;
    private final IdProvider idProvider;

    private final boolean followRefs;

    public JsonInstanceContext (URI scope, ReferenceRegistry references) {
        this.scope = scope;
        this.references = references;
        this.idProvider = new IdProvider ();

        followRefs = !this.references.isEmpty ();
        if (followRefs) {
            visitRef (scope);
        }
    }

    public JsonInstanceContext (URI scope, ReferenceRegistry references, boolean followRefs) {
        this.scope = scope;
        this.references = references;
        this.idProvider = new IdProvider ();

        this.followRefs = followRefs;
        if (followRefs) {
            visitRef (scope);
        }
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

    public JsonInstanceContext withScope (@Nullable URI targetScope) {
        // todo possible??
        if (targetScope == null) {
            return this;
        }
        return new JsonInstanceContext (scope.resolve (targetScope), references);
    }

    public JsonInstanceContext withId (Map<String, Object> properties) {
        String id = idProvider.getId (properties);
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
        if (!followRefs)
            return false;

        if (!hasReference (ref))
            return false;

        Reference reference = getReference (ref);
        return reference.visitCnt == 0;
    }

    public void visitRef(URI ref) {
        if (!followRefs)
            return;

        if (!hasReference (ref))
            return;

        Reference reference = getReference (ref);
        reference.visitCnt += 1;
    }

}

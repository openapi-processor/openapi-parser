/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import io.openapiparser.converter.StringNullableConverter;
import io.openapiparser.schema.*;
import io.openapiparser.schema.Reference;
import io.openapiparser.schema.ReferenceRegistry;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;
import java.util.Map;

import static io.openapiparser.schema.Keywords.REF;
import static io.openapiparser.schema.UriSupport.createUri;

/**
 * the context is used to resolve $ref's.
 *
 * All model objects have a context. Objects from the same source file will re-use the same context.
 * Objects from different source files will have different {@link Context} objects. If a $ref links
 * to another source file the "current" context will create a new context with the new source file.
 */
public class Context {
    private final URI scope;
    private final ReferenceRegistry references;
    private final IdProvider idProvider;

    public Context (URI scope, ReferenceRegistry references) {
        this.scope = scope;
        this.references = references;
        this.idProvider = new IdProvider ();
    }

    public Reference getReference (URI ref) {
        URI resolved = scope.resolve (ref);
        return references.getRef (resolved);
    }

    public @Nullable Bucket getRefObjectOrNull (Bucket bucket) {
        String ref = bucket.convert (REF, new StringNullableConverter ());
        if (ref == null)
            return null;

        return getRefObject (ref);
    }

    public Bucket getRefObjectOrThrow (Bucket bucket) {
        final Bucket refObject = getRefObjectOrNull (bucket);
        if (refObject == null)
            throw new RuntimeException ();

        return refObject;
    }

    public JsonInstanceContext getInstanceContext () {
        return new JsonInstanceContext (scope, references);
    }

    private @Nullable Bucket getRefObject(String ref) {
        Reference reference = getReference (createUri (ref));
        Context refContext = withScope (reference.getValueScope ());

        Map<String, Object> value = reference.getValue ();
        if (value == null) {
            // todo if it is null it was not resolved, throw?
            return null;
        }
        return new Bucket (refContext.withId(value), reference.getPointer (), value);
    }

    /**
     * get a context with the given scope.
     *
     * If this context has the same scope it will return itself otherwise it will create a new
     * context with the given scope.
     *
     * @param targetScope the new scope uri.
     * @return context with the scope uri
     */
    public Context withScope (@Nullable URI targetScope) {
        // todo possible??
        if (targetScope == null) {
            return this;
        }

        return new Context (scope.resolve (targetScope), references);
    }

    public URI withId (Map<String, Object> properties) {
        String id = idProvider.getId (properties);
        if (id == null) {
            return scope;
        }

        return withId (id);
    }

    public URI withId (@Nullable String id) {
        if (id == null) {
            return scope;
        }
        return scope.resolve (id);
    }
}

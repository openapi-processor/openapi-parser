/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import io.openapiprocessor.jsonschema.converter.UriConverter;
import io.openapiprocessor.jsonschema.schema.*;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;

import static io.openapiprocessor.jsonschema.schema.Keywords.REF;
import static io.openapiprocessor.jsonschema.support.Nullness.nonNull;

/**
 * the context is used to resolve $ref's.
 * All model objects have a context. Objects from the same source file will re-use the same context.
 * Objects from different source files will have different {@link Context} objects. If a $ref links
 * to another source file the "current" context will create a new context with the new source file.
 */
public class Context {
    private final Scope scope;
    private final ReferenceRegistry references;

    public Context (Scope scope, ReferenceRegistry references) {
        this.scope = scope;
        this.references = references;
    }

    public Bucket getRefObjectOrThrow (Bucket bucket) {
        final Bucket refObject = getRefObjectOrNull (bucket);
        if (refObject == null)
            throw new RuntimeException ();

        return refObject;
    }

    public @Nullable Bucket getRefObjectOrNull (Bucket bucket) {
        JsonSchemaContext ctx = new JsonSchemaContext (bucket.getScope (), references);
        Reference reference = ctx.getReference (nonNull (getRef (bucket)));
        return Bucket.toBucket (reference.getValueScope (), reference.getValue (), reference.getPointer ()); // todo value is nullable ?
    }

    public @Nullable URI getRef (Bucket bucket) {
        return bucket.convert (REF, new UriConverter ());
    }

    public Reference getReference (Bucket bucket) {
        JsonSchemaContext ctx = new JsonSchemaContext (bucket.getScope (), references);
        return ctx.getReference (nonNull (getRef (bucket)));
    }

    public JsonInstanceContext getInstanceContext () {
        return new JsonInstanceContext (scope, references);
    }

    /**
     * get a context with the given scope. If this context has the same scope it will return itself otherwise it will
     * create a new context with the given scope.
     *
     * @param targetScope the new scope uri.
     * @return context with the scope uri
     */

    public Context withScope (Scope targetScope) {
        return new Context (targetScope, references);
    }
}

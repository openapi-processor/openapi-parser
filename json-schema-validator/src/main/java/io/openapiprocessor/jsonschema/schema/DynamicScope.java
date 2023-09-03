/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.schema;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import static io.openapiprocessor.jsonschema.support.Uris.createUri;
import static io.openapiprocessor.jsonschema.support.Uris.emptyFragment;

public final class DynamicScope {

    private static class Scope {
        private final JsonSchema schema;

        Scope (JsonSchema schema) {
            this.schema = schema;
        }

        boolean matches (String anchor) {
            JsonSchemaContext context = schema.getContext ();
            return context.hasDynamicReference (anchor);
        }

        boolean equal (JsonSchema other) {
            return getBaseUri (schema).equals (getBaseUri (other));
        }

        URI getBaseUri () {
            return getBaseUri (schema);
        }

        URI getBaseUri (JsonSchema source) {
            return source.getContext ().getScope ().getBaseUri ();
        }
    }

    private final List<Scope> scopes = new ArrayList<> ();

    public DynamicScope (JsonSchema schema) {
        scopes.add (new Scope (schema));
    }

    private DynamicScope (DynamicScope source) {
        scopes.addAll (source.scopes);
    }

    public DynamicScope add (JsonSchema schema) {
        if (scopes.size () == 0) {
            return this;
        }

        Scope last = scopes.get (scopes.size () - 1);
        if (last.equal (schema)) {
            return this;
        }

        DynamicScope dynamicScope = new DynamicScope (this);
        Scope scope = new Scope (schema);
        dynamicScope.scopes.add (scope);
        return dynamicScope;
    }

    public @Nullable URI findScope (URI dynamicRef) {
        Scope match = null;
        URI dynamicAnchor;
        if (dynamicRef.equals (emptyFragment ())) {
            dynamicAnchor = dynamicRef;
        } else {
            dynamicAnchor = createUri ("#" + dynamicRef.getFragment ());
        }

        boolean first = true;
        ListIterator<Scope> lit = scopes.listIterator (scopes.size ());
        while(lit.hasPrevious()) {
            Scope previous = lit.previous ();

            if (first) {
                first = false;

                // find start scope, full dynamic ref
                boolean hasDynamicAnchor = previous.matches (dynamicRef.toString ());
                if (hasDynamicAnchor) {
                    match = previous;
                } else {
                    // start scope has no matching dynamicAnchor, handle as $ref
                    return null;
                }
            } else {
                // find outermost scope, #fragment only
                boolean hasDynamicAnchor = previous.matches (dynamicAnchor.toString ());
                if (hasDynamicAnchor) {
                    match = previous;
                }
            }
        }

        if (match == null)
            return null;

        return match.getBaseUri ();
    }

    @Override
    public String toString () {
        return String.format ("length %d", scopes.size ());
    }
}

/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;
import java.util.*;

public final class DynamicScope {

    private static class Scope {
        private final URI id;
        private final @Nullable String fragment;

        Scope (URI id) {
            this.id = id;
            this.fragment = null;
        }
        Scope (URI id, @Nullable String dynamicAnchor) {
            this.id = id;
            this.fragment = dynamicAnchor;
        }

        boolean matches (String fragment) {
            return fragment.equals (this.fragment);
        }
    }

    private final List<Scope> scopes = new ArrayList<> ();

    public DynamicScope (JsonSchema schema) {
        Scope scope = createScope (schema);
        if (scope == null) {
            scope = new Scope (getBaseUri (schema));
        }

        scopes.add (scope);
    }

    private DynamicScope (DynamicScope source) {
        scopes.addAll (source.scopes);
    }

    public DynamicScope add (JsonSchema schema) {
        Scope scope = createScope (schema);
        if (scope == null) {
            return this;
        }

        DynamicScope dynamicScope = new DynamicScope (this);
        dynamicScope.scopes.add (scope);
        return dynamicScope;
    }

    private @Nullable Scope createScope (JsonSchema schema) {
        URI id = schema.getId ();
        if (id == null) {
            return null;
        }

        return new Scope (getBaseUri (schema), schema.getDynamicAnchor ());
    }

    public @Nullable URI findScope (URI fragment) {
        ListIterator<Scope> lit = scopes.listIterator (scopes.size ());

        Scope match = null;
        while(lit.hasPrevious()) {
            Scope previous = lit.previous ();
            if (previous.matches (fragment.toString ())) {
                match = previous;
            } else {
                if (match == null) {
                    match = previous;
                }
                break;
            }
        }

        if (match == null)
            return null;

        return match.id;
    }

    private URI getBaseUri(JsonSchema schema) {
        return schema.getContext ().getScope ().getBaseUri ();
    }

    @Override
    public String toString () {
        return String.format ("length %d", scopes.size ());
    }
}

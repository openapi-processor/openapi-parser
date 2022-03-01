/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import java.net.URI;

import static io.openapiparser.schema.Keywords.HASH;

/**
 * $ref support.
 */
public class Ref {
    public static final String START_OF_POINTER = "#/";

    private final URI scope;
    private final String ref;

    /**
     * create a ref with empty scope.
     *
     * @param ref a ref
     */
    public Ref (String ref) {
        this.scope = URI.create ("");
        this.ref = ref;
    }

    /**
     * create ref with scope.
     *
     * @param scope scope uri
     * @param ref a ref
     */
    public Ref (URI scope, String ref) {
        this.scope = scope;
        this.ref = ref;
    }

    /**
     * create ref with scope.
     *
     * @param scope scope
     * @param ref the ref
     */
    public Ref (String scope, String ref) {
        this.scope = URI.create (scope);
        this.ref = ref;
    }

    /**
     * checks if the ref has a json pointer.
     *
     * @return true if ref has a json pointer
     */
    public boolean hasPointer () {
        return ref.contains (START_OF_POINTER);
    }

    /**
     * get the json pointer.
     *
     * @return json pointer
     */
    public String getPointer () {
        if (hasPointer ()) {
            return ref.substring(ref.indexOf (HASH));
        } else {
            return "";
        }
    }

    /**
     * checks if the ref has a document.
     *
     * @return true if ref has a document, else false
     */
    public boolean hasDocument () {
        return ref.indexOf (START_OF_POINTER) != 0;
    }

    /**
     * get the document uri of this ref. It resolves the ref on the scope and strips the fragment.
     *
     * @return the document uri of this ref
     */
    public URI getDocumentUri () {
        String absoluteRef = getFullRef ();

        if (absoluteRef.equals (HASH)) {
            return URI.create ("");
        }

        // starts with # but not #/
        if (absoluteRef.indexOf (HASH) == 0 && !hasPointer ()) {
            return getFullRefUri ();
        }

        return withoutFragment ();
    }

    /**
     * get the original ref value.
     *
     * @return the ref
     */
    public String getRef () {
        return ref;
    }

    /**
     * get full ref uri with scope and json pointer.
     *
     * @return the full ref uri
     */
    public String getFullRef () {
        return getFullRefUri ().toString ();
    }

    /**
     * get full ref uri with scope and json pointer.
     *
     * @return the full ref uri
     */
    public URI getFullRefUri () {
        return scope.resolve (ref);
    }

    @Override
    public String toString () {
        return scope.resolve (ref).toString ();
    }

    private URI withoutFragment () {
        String absoluteRef = getFullRef ();
        int index = absoluteRef.indexOf (HASH);
        if (index < 0) {
            return getFullRefUri ();
        }

        return URI.create (absoluteRef.substring (0, index));
    }
}

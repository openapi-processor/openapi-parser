/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.schema;

import io.openapiprocessor.jsonschema.support.UriSupport;

import java.net.URI;

import static io.openapiprocessor.jsonschema.schema.Keywords.HASH;
import static io.openapiprocessor.jsonschema.support.UriSupport.*;

/**
 * $ref support.
 */
public class Ref {
    public static final String START_OF_POINTER = "#/";
    private static final String EMPTY_REF = "";
    private static final URI EMPTY_REF_URI = URI.create (EMPTY_REF);

    private final Scope scope;  // scope in which the ref was found
    private final String ref;   // do not use
    private final URI refUri;

    /**
     * create ref with scope.
     *
     * @param scope scope
     * @param ref the ref
     */
    public Ref (Scope scope, String ref) {
        this.scope = scope;
        this.ref = ref;
        this.refUri = createUri (ref);
    }

    /**
     * create ref with scope.
     *
     * @param scope scope
     * @param ref the ref uri
     */
    public Ref (Scope scope, URI ref) {
        this.scope = scope;
        this.ref = ref.toString ();
        this.refUri = ref;
    }

    /**
     * create empty ref with scope.
     *
     * @param scope scope
     */
    public Ref (Scope scope) {
        this (scope, EMPTY_REF);
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
        return stripFragment (getAbsoluteUri ());
    }

    /**
     * get the scope of the ref.
     *
     * @return scope
     */
    public Scope getScope () {
        return scope;
    }

    /**
     * get the base uri of the ref.
     *
     * @return base uri
     */
    public URI getBaseUri () {
        return scope.getBaseUri ();
    }

    /**
     * get the original ref value.
     *
     * @return the ref
     */
    public String getRef () {
        return ref;
    }

    public URI getRefUri () {
        return refUri;
    }

    public String getAbsoluteRef () {
        return getAbsoluteUri ().toString ();
    }

    public URI getAbsoluteUri () {
        if (ref.isEmpty ()) {
            return scope.getBaseUri ();
        }

        return UriSupport.resolve(scope.getBaseUri(), refUri);
    }

    @Override
    public String toString () {
        return getAbsoluteUri ().toString ();
    }
}

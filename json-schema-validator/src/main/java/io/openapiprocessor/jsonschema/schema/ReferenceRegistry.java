/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.schema;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;
import java.util.*;

import static io.openapiprocessor.jsonschema.schema.UriSupport.hasEmptyFragment;
import static io.openapiprocessor.jsonschema.schema.UriSupport.stripFragment;

/**
 * holds all resolved references of a document.
 */
public class ReferenceRegistry {
    static class ReferenceKey {
        URI uri;
        ReferenceType type;

        public ReferenceKey (URI uri, ReferenceType type) {
            this.uri = uri;
            this.type = type;
        }

        @Override
        public boolean equals (@Nullable Object o) {
            if (this == o)
                return true;

            if (o == null || getClass () != o.getClass ())
                return false;

            ReferenceKey that = (ReferenceKey) o;
            return Objects.equals (uri, that.uri)
                && type == that.type;
        }

        @Override
        public int hashCode () {
            return Objects.hash (uri, type);
        }

        @Override
        public String toString () {
            return String.format ("%s (%s)", uri, type);
        }
    }

    private final Map<ReferenceKey, Reference> references = new HashMap<> ();

    public boolean isEmpty() {
        return references.isEmpty ();
    }

    /**
     * check if {@code absoluteRef} is resolved. If the ref has an empty fragment (#) it checks
     * with and without the empty fragment.
     *
     * @param absoluteRef absolute ref uri
     * @return true if the ref is resolved, otherwise false.
     */
    public boolean hasReference (URI absoluteRef) {
        boolean hasRef = hasReference (absoluteRef, ReferenceType.STATIC);
        if (hasRef)
            return true;

        if (hasEmptyFragment (absoluteRef))
            return hasReference (stripFragment (absoluteRef), ReferenceType.STATIC);

        return false;
    }

    /**
     * get a static reference.
     *
     * @param absoluteRef absolute ref uri
     * @return the reference
     */
    public Reference getReference (URI absoluteRef) {
        Reference reference = getReference (absoluteRef, ReferenceType.STATIC);
        if (reference != null)
            return reference;

        if (hasEmptyFragment (absoluteRef))
            reference = getReference (stripFragment (absoluteRef), ReferenceType.STATIC);

        if (reference == null)
            throw new RuntimeException (); // todo

        return reference;
    }

    /**
     * get a dynamic reference.
     *
     * @param absoluteRef absolute ref uri
     * @return the reference
     */
    public Reference getDynamicReference (URI absoluteRef) {
        Reference reference = getReference (absoluteRef, ReferenceType.DYNAMIC);
        if (reference != null)
            return reference;

        return getReference (absoluteRef);
    }

    public void addReference (Ref ref, Scope valueScope, Object document) {
        references.put (
            new ReferenceKey (ref.getAbsoluteUri (), ReferenceType.STATIC),
            new Reference (ref, new RefValue(valueScope, document))
        );
    }

    public void addDynamicReference (Ref ref, Scope valueScope, Object document) {
        references.put (
            new ReferenceKey (ref.getAbsoluteUri (), ReferenceType.DYNAMIC),
            new Reference (ref, new RefValue(valueScope, document))
        );
    }

    public boolean hasDynamicReference (URI uri) {
        return hasReference (uri, ReferenceType.DYNAMIC);
    }

    private boolean hasReference (URI uri, ReferenceType type) {
        return references.containsKey (new ReferenceKey (uri, type));
    }

    private @Nullable Reference getReference (URI uri, ReferenceType type) {
        return references.get (new ReferenceKey (uri, type));
    }
}

/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;
import java.util.*;

import static io.openapiparser.schema.UriSupport.hasEmptyFragment;
import static io.openapiparser.schema.UriSupport.stripFragment;

/**
 * holds all resolved references of a document.
 */
public class ReferenceRegistry {
    private final Map<String, Reference> references = new HashMap<> ();  // URI key???

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
        boolean hasRef = hasReferenceX (absoluteRef);
        if (hasRef)
            return true;

        if (hasEmptyFragment (absoluteRef))
            return hasReferenceX (stripFragment (absoluteRef));

        return false;
    }

    public boolean contains (URI absoluteRef) {
        return hasReference (absoluteRef);
    }

    @Deprecated
    public boolean hasRef (URI absoluteRef) {
        return hasReference (absoluteRef);
    }

    /**
     * get a reference.
     *
     * @param absoluteRef absolute ref uri
     * @return the reference
     */
    public Reference getReference (URI absoluteRef) {
        Reference reference = getReferenceX (absoluteRef);
        if (reference != null)
            return reference;

        if (hasEmptyFragment (absoluteRef))
            reference = getReferenceX (stripFragment (absoluteRef));

        if (reference == null)
            throw new RuntimeException (); // todo

        return reference;
    }

    @Deprecated
    public Reference getRef (URI absoluteRef) {
        return getReference (absoluteRef);
    }

    public void addReference (Ref ref, Scope valueScope, Object document) {
        add (ref, valueScope, document);
    }

    public void add (Ref ref, Scope valueScope, Object document) {
        references.put (
            ref.getAbsoluteUri ().toString (),
            new Reference (ref, new RefValue(valueScope, document))
        );
    }

    private boolean hasReferenceX (URI uri) {
        return references.containsKey (uri.toString ());
    }

    private @Nullable Reference getReferenceX (URI uri) {
        return references.get (uri.toString ());
    }
}

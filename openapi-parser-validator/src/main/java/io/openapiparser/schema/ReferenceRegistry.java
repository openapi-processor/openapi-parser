/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import java.net.URI;
import java.util.*;
import java.util.function.Function;

/**
 * holds a document and all its references.
 */
public class ReferenceRegistry {

    private static class Pending {
        Ref ref;

        public Pending(Ref ref) {
            this.ref = ref;
        }

        @Override
        public boolean equals (Object o) {
            if (this == o)
                return true;

            if (o == null || getClass () != o.getClass ())
                return false;

            Pending pending = (Pending) o;
            return ref.getFullRefUri ().equals (pending.ref.getFullRefUri ());
        }

        @Override
        public int hashCode () {
            return Objects.hash (ref.getFullRef ());
        }
    }

    private final Set<Pending> pending = new HashSet<> ();
    private final Map<String, Reference> references = new HashMap<> ();


    public void add (Ref ref) {
        pending.add (new Pending (ref));
    }

    /**
     * resolves the references {@link #add(Ref)}ed to the registry.
     *
     * @param resolver resolver callback that resolves the ref
     */
    public void resolve (Function<Ref, Object> resolver) {
        Iterator<Pending> iterator = pending.iterator ();
        while (iterator.hasNext ()) {
            Pending next = iterator.next ();
            add (next, resolver.apply (next.ref));
            iterator.remove ();
        }
    }

    /**
     * get a reference from the registry.
     *
     * @param absoluteRef absolute uri of the ref
     * @return the reference
     */
    public Reference getRef (URI absoluteRef) {
        return getRef (absoluteRef.toString ());
    }

    private Reference getRef(String absoluteRef) {
        Reference reference = references.get (absoluteRef);
        if (reference == null)
            throw new RuntimeException (); // todo

        return reference;
    }

    private void add (Pending ref, Object value) {
        Ref aRef = ref.ref;
        references.put (aRef.getFullRef (), createReference (ref, value));
    }

    private Reference createReference (Pending pending, Object value) {
        return new Reference (pending.ref, value);
    }
}

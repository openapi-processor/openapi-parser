/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import java.net.URI;
import java.util.*;
import java.util.function.BiFunction;

/**
 * holds a document and all its references.
 */
public class ReferenceRegistry {

    private static class Pending {
        URI parentUri;
        URI documentUri;
        String ref;

        public Pending (URI parentUri, URI documentUri, String ref) {
            this.parentUri = parentUri;
            this.documentUri = documentUri;
            this.ref = ref;
        }
    }

    private final List<Pending> pending = new ArrayList<> ();
    private final Map<String, Reference> references = new HashMap<> ();

    /*
     * add the base document. the uri is the "ref" to get the document from the registry.
     *
     * @param uri document uri
     * @param document content of the document
     */
//    public void add (URI uri, Object document) {
//        references.put (uri.toString (), new Reference (uri, uri, "", document));
//    }

    /**
     * add an unresolved ref. The {@code documentRef} is the key to get the {@link Reference} from
     * the registry.
     *
     * @param parentUri parent document uri
     * @param documentUri absolute document uri without ref
     * @param ref ref in the document
     */
    public void add (URI parentUri, URI documentUri, String ref) {
        pending.add (new Pending (parentUri, documentUri, ref));
    }

    /**
     * resolves the references {@link #add(URI, URI, String)}ed to the registry.
     *
     * @param resolver resolver callback that resolves the ref
     */
    public void resolve (BiFunction<URI, String, Object> resolver) {
        ListIterator<Pending> iterator = pending.listIterator ();
        while (iterator.hasNext ()) {
            Pending next = iterator.next ();
            add (next, resolver.apply (next.documentUri, next.ref));
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
        references.put (
            createAbsoluteRefUri (ref.documentUri, ref.ref),
            new Reference (ref.parentUri, ref.documentUri, ref.ref, value));
    }

    private String createAbsoluteRefUri(URI documentUri, String ref) {
        int hash = ref.indexOf("#");
        if (hash == -1) {
            return documentUri.toString ();
        }

        String hashPart = ref.substring(hash);
        String encoded = hashPart;
//            .replace ("{", "%7B")
//            .replace ("}", "%7D");

        return documentUri.resolve (URI.create (encoded)).toString ();
    }
}

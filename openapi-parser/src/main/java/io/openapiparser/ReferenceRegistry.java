/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

@Deprecated
public class ReferenceRegistry {
    private final Map<String, Reference> references = new HashMap<> ();

    public ReferenceRegistry() {
    }

    public void add (URI parentUri, URI documentUri, String ref) {
        references.put (
            createAbsoluteRefUri (documentUri, ref),
            new Reference (parentUri, documentUri, ref));
    }

    public void resolve(BiFunction<URI, String, Object> resolver) {
        references.forEach ((key, ref) -> {
            replace (key, ref, resolver.apply (ref.getDocumentUri (), ref.getRef ()));
        });
    }

    Reference getRef (URI absoluteRef) {
        return getRef (absoluteRef.toString ());
    }

    private Reference getRef(String absoluteRef) {
        Reference reference = references.get (absoluteRef);
        if (reference == null)
            throw new RuntimeException (); // todo

        return reference;
    }

    private void replace (String key, Reference ref, Object rawValue) {
        references.put (key, ref.withRawValue (rawValue));
    }

    private String createAbsoluteRefUri(URI documentUri, String ref) {
        int hash = ref.indexOf("#");
        if (hash == -1) {
            return documentUri.toString ();
        }

        String hashPart = ref.substring(hash);
        String encoded = hashPart
            .replace ("{", "%7B")
            .replace ("}", "%7D");

        return documentUri.resolve (URI.create (encoded)).toString ();
    }
}

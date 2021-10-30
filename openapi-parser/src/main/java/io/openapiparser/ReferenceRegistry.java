/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class ReferenceRegistry {
    private final URI baseUri; // todo not needed ??
    private final Map<String, Reference> references = new HashMap<> ();

    public ReferenceRegistry(URI baseUri) {
      this.baseUri = baseUri;
    }

    public void add (URI parentUri, URI documentUri, String ref) {
        references.put (
            createAbsoluteRefUri (documentUri, ref),
            new Reference (parentUri, documentUri, ref));
    }

    public void resolve(BiFunction<URI, String, Object> resolver) {
        references.forEach ((key, ref) -> {
            replace (key, ref, resolver.apply (ref.getDocUri (), ref.getRef ()));
        });
    }

    // todo where does a caller get the absolute ref?
    Reference getRef(String absoluteRef) {
        return references.get (absoluteRef);
    }

    private void replace (String key, Reference ref, Object rawValue) {
        references.put (key, ref.withRawValue (rawValue));
    }

    private String createAbsoluteRefUri(URI documentUri, String ref) {
        final int hash = ref.indexOf("#");
        if (hash == -1) {
            return documentUri.toString ();
        }

        return documentUri.resolve (URI.create (ref.substring(hash))).toString ();
    }

}

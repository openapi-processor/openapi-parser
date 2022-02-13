/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * repository for "downloaded" documents. Used to cache documents and avoid downloading the same
 * document multiple times.
 *
 * a document may be a single value or a whole object tree.
 */
public class DocumentStore {
    private final Map<URI, Object> documents = new HashMap<> ();

    private static class LazyDocument { }

    private static LazyDocument PENDING = new LazyDocument ();

    public void add (URI uri) {
        documents.put (uri, PENDING);
    }

    public void add (URI uri, Object document) {
        documents.put (uri, document);
    }

    public Object get (URI uri) {
        final Object document = documents.get (uri);
        if (document == null)
            // todo
            throw new RuntimeException ();

        return document;
    }

    public boolean contains (URI uri) {
        Object d = documents.get (uri);
        if (d == null)
            return false;

        return d != PENDING;
    }
}

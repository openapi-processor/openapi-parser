/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import io.openapiparser.schema.Bucket;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Deprecated
public class DocumentRegistry {
    private final Map<URI, Bucket> documents = new HashMap<> ();

    public void add (URI uri, Bucket document) {
        documents.put (uri, document);
    }

    public Bucket get (URI uri) {
        final Bucket document = documents.get (uri);
        if (document == null)
            // todo
            throw new RuntimeException ();

        return document;
    }

    public boolean contains (URI uri) {
        return documents.containsKey (uri);
    }
}

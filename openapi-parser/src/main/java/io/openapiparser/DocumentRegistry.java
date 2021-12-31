/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import io.openapiparser.schema.PropertyBucket;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class DocumentRegistry {
    private final Map<URI, PropertyBucket> documents = new HashMap<> ();

    public void add (URI uri, PropertyBucket document) {
        documents.put (uri, document);
    }

    public PropertyBucket get (URI uri) {
        final PropertyBucket document = documents.get (uri);
        if (document == null)
            // todo
            throw new RuntimeException ();

        return document;
    }

    public boolean contains (URI uri) {
        return documents.containsKey (uri);
    }
}

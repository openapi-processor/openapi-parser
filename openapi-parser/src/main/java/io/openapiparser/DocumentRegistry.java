/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class DocumentRegistry {
    private final Map<URI, Node> documents = new HashMap<> ();

    public void add (URI uri, Node node) {
        documents.put (uri, node);
    }

    public Node get (URI uri) {
        return documents.get (uri);
    }

    public boolean contains (URI uri) {
        return documents.containsKey (uri);
    }
}

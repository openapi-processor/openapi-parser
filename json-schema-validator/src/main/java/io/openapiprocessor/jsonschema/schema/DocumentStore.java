/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.schema;

import io.openapiprocessor.jsonschema.support.Copy;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static io.openapiprocessor.jsonschema.schema.Document.Source.*;

/**
 * repository for "downloaded" documents. Used to cache documents and avoid downloading the same
 * document multiple times. A document may be a single value or a whole object tree.
 */
public class DocumentStore {
    private final Map<URI, Document> documents = new HashMap<> ();

    public void addId (URI uri, Object document) {
        documents.put (uri, new Document (document, ID));
    }

    public void addAnchor (URI uri, Object document) {
        documents.put (uri, new Document (document, ANCHOR));
    }

    public void addDynamicAnchor (URI uri, Object document) {
        documents.put (uri, new Document (document, DYNAMIC_ANCHOR));
    }

    public @Nullable Document getDocument (URI uri) {
        return documents.get (uri);
    }

    public @Nullable Object get (URI uri) {
        Document document = documents.get (uri);

        if (document == null) {
            // check if registered with empty fragment
            if (uri.getFragment () == null) {
                URI resolved = uri.resolve ("#");
                document = documents.get (resolved);
            }
        }

        if (document == null) {
            return null;
        }

        return document.getDocument ();
    }

    public boolean isEmpty () {
        return documents.isEmpty ();
    }

    public boolean contains (URI uri) {
        return documents.containsKey (uri);
    }

    public Map<URI, Document> getDocuments() {
        return documents;
    }

    public DocumentStore copy () {
        DocumentStore store = new DocumentStore ();
        documents.forEach ((key, value) -> {
            store.documents.put (key, new Document (Copy.deep (value.getDocument ()), value.getSource ()));
        });
        return store;
    }
}

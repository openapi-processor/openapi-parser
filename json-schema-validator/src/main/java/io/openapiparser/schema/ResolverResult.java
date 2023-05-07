/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

public class ResolverResult {
    private final Scope scope;
    private final Object document;
    private final ReferenceRegistry registry;
    private final DocumentStore documents;

    public ResolverResult (Scope scope, Object document, ReferenceRegistry registry, DocumentStore documents) {
        this.scope = scope;
        this.document = document;
        this.registry = registry;
        this.documents = documents;
    }

    public Scope getScope () {
        return scope;
    }

    public Object getDocument () {
        return document;
    }

    public ReferenceRegistry getRegistry () {
        return registry;
    }

    public DocumentStore getDocuments () {
        return documents;
    }
}

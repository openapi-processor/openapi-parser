/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import java.net.URI;

public class ResolverResult {
    private final URI uri;
    private final Object document;
    private final ReferenceRegistry registry;

    public ResolverResult (URI uri, Object document, ReferenceRegistry registry) {
        this.uri = uri;
        this.document = document;
        this.registry = registry;
    }

    public URI getUri () {
        return uri;
    }

    public Object getDocument () {
        return document;
    }

    public ReferenceRegistry getRegistry () {
        return registry;
    }
}

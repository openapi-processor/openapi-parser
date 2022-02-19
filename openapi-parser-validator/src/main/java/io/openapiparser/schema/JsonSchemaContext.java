/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import java.net.URI;

public class JsonSchemaContext {
    private final URI baseUri;
    private final ReferenceRegistry references;

    public JsonSchemaContext (URI baseUri, ReferenceRegistry references) {
        this.baseUri = baseUri;
        this.references = references;
    }

    public Reference getReference (URI ref) {
        URI resolved = baseUri.resolve (ref);
        return references.getRef (resolved);
    }

    public JsonSchemaContext withSource (URI source) {
        if (baseUri.equals (source)) {
            return this;
        }
        return new JsonSchemaContext (source, references);
    }
}

/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import java.net.URI;

public class JsonSchemaContext {
    private final URI scope;
    private final ReferenceRegistry references;

    public JsonSchemaContext (URI scope, ReferenceRegistry references) {
        this.scope = scope;
        this.references = references;
    }

    public URI getScope () {
        return scope;
    }

    public Reference getReference (URI ref) {
        URI resolved = scope.resolve (ref);
        return references.getRef (resolved);
    }

    public JsonSchemaContext withSource (URI source) {
        if (scope.equals (source)) {
            return this;
        }
        return new JsonSchemaContext (source, references);
    }
}

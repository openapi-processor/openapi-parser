/*
 * Copyright 2023 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.schema;

import java.net.URI;

public class SchemaResource {
    private final URI uri;
    private final String resource;

    public SchemaResource (String uri, String resource) {
        this.uri = URI.create (uri);
        this.resource = resource;
    }

    public URI getUri () {
        return uri;
    }

    public String getResource () {
        return resource;
    }
}

/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import java.net.URI;

public enum SchemaVersion {
    None (""),
    Draft4("http://json-schema.org/draft-04/schema#"),
    Draft6("http://json-schema.org/draft-06/schema#");

    private final URI schema;

    private
    SchemaVersion(String schema) {
        this.schema = URI.create (schema);
    }

    public URI getSchema () {
        return schema;
    }
}




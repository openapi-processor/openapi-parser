/*
 * Copyright 2024 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.schema;

import java.net.URI;

public class NotRegisteredException extends RuntimeException {
    public NotRegisteredException(URI schemaUri) {
        super(String.format("Schema %s is not registered in the SchemaStore", schemaUri));
    }
}

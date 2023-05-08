/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.schema;

/**
 * thrown if the schema store is unable to load a json schema.
 */
public class SchemaStoreException extends RuntimeException {

    public SchemaStoreException (String message) {
        super(message);
    }

    public SchemaStoreException (String message, Exception e) {
        super(message, e);
    }
}

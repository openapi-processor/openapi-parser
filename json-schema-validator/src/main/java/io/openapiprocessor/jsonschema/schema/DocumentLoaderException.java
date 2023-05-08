/*
 * Copyright 2023 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.schema;

public class DocumentLoaderException extends RuntimeException {

    public DocumentLoaderException (String message) {
        super(message);
    }

    public DocumentLoaderException (String message, Exception e) {
        super(message, e);
    }
}

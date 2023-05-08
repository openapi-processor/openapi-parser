/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.schema;

public class InvalidPropertyException extends RuntimeException {

    public InvalidPropertyException (String message) {
        super(message);
    }

    public InvalidPropertyException (String message, Exception e) {
        super(message, e);
    }

    public InvalidPropertyException (JsonPointer location) {
        this (location.toString ());
    }
}

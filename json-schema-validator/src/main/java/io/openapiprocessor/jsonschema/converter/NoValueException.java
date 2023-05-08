/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.converter;

import io.openapiprocessor.jsonschema.schema.JsonPointer;

/**
 * thrown if a property has no value ({@code null} or is missing).
 */
public class NoValueException extends RuntimeException {

    public NoValueException (String location) {
        super (String.format ("property '%s' has no value", location));
    }

    public NoValueException (JsonPointer location) {
        this (location.toString ());
    }
}

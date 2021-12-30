/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.converter;

/**
 * thrown if a property has no value ({@code null} or is missing).
 */
public class NoValueException extends RuntimeException {

    public NoValueException (String property) {
        super(String.format ("property '%s' has no value", property));
    }
}

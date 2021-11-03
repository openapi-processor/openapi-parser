/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

/**
 * thrown if a property is {@code null} when it should not be null.
 */
public class NullValueException extends RuntimeException {

    public NullValueException (String property) {
        super(String.format ("property '%s' should not be null", property));
    }
}

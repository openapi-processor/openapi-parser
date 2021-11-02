/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

/**
 * thrown if a property cast from {@link Object} to a concrete type fails.
 */
public class BadPropertyTypeException extends RuntimeException {

    public BadPropertyTypeException (String property, String type) {
        super(String.format ("property '%s' is not a %s", property, type));
    }

    public BadPropertyTypeException (String property, Class<?> type) {
        super(String.format ("property '%s' is not a %s", property, type.getName ()));
    }
}

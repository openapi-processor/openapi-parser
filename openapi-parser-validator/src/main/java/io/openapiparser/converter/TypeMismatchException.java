/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.converter;

/**
 * thrown if type conversion (i.e. cast) from {@link Object} to the required type fails.
 */
public class TypeMismatchException extends RuntimeException {

    public TypeMismatchException (String property, String type) {
        super(String.format ("property '%s' is not a %s", property, type));
    }

    public TypeMismatchException (String property, Class<?> type) {
        super(String.format ("property '%s' is not a %s", property, type.getName ()));
    }
}

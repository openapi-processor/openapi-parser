/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

/**
 * thrown if a property is not a json/yaml array, i.e. not a {@code Collection<Object>}.
 */
public class NoArrayException extends RuntimeException {

    public NoArrayException (String property) {
        super(String.format ("property %s should be an array", property));
    }
}

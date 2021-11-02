/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

/**
 * thrown if a property is not a json/yaml object, i.e. not a {@code Map<String,Object>}.
 */
public class NoObjectException extends RuntimeException {

    public NoObjectException (String property) {
        super(String.format ("property '%s' should be an object", property));
    }
}

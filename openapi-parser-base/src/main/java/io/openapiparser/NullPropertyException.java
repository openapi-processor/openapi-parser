/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

/**
 * thrown if a property is {@code null} when it should not be null.
 */
public class NullPropertyException extends RuntimeException {

    public NullPropertyException (String message) {
        super(message);
    }

}

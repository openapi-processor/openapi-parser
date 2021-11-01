/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

/**
 * thrown if the parser is unable to resolve a $reference.
 */
public class ResolverException extends RuntimeException {

    public ResolverException (String message) {
        super(message);
    }

    public ResolverException (String message, Exception e) {
        super(message, e);
    }
}

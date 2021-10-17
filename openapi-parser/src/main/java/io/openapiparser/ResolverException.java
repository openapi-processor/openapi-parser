/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

public class ResolverException extends Exception {

    public ResolverException (String message, Exception e) {
        super(message, e);
    }
}

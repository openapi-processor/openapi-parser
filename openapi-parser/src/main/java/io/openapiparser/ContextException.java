/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

public class ContextException extends Exception {

    public ContextException (String message) {
        super(message);
    }

    public ContextException (String message, Exception e) {
        super(message, e);
    }
}

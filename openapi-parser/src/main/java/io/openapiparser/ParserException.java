/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

public class ParserException extends RuntimeException {
    public ParserException (Exception e) {
        super("failed to parse OpenAPI description", e);
    }
}

/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import java.net.URI;

public class ParserException extends RuntimeException {
    public ParserException (Exception e) {
        super("failed to parse OpenAPI description", e);
    }

    public ParserException (URI documentUri, Exception e) {
        super(String.format("failed to parse OpenAPI description %s", documentUri) , e);
    }
}

/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

public class JsonPointerInvalidException extends RuntimeException {

    public JsonPointerInvalidException (String pointer) {
        super(String.format ("invalid json pointer: %s", pointer));
    }

    public JsonPointerInvalidException (String pointer, Exception ex) {
        super(String.format ("invalid json pointer: %s", pointer), ex);
    }

    public JsonPointerInvalidException (String pointer, String token, Exception ex) {
        super(String.format ("invalid json pointer: %s at token %s.", pointer, token), ex);
    }
}

/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

public class JsonSchemaBoolean implements JsonSchema {
    private final JsonPointer pointer;
    private final boolean content;

    public JsonSchemaBoolean (Boolean content) {
        this.pointer = JsonPointer.EMPTY;
        this.content = content;
    }

    public JsonSchemaBoolean (JsonPointer pointer, Boolean content) {
        this.pointer = pointer;
        this.content = content;
    }

    @Override
    public boolean isFalse () {
        return !content;
    }
}

/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

public class JsonSchemaBoolean implements JsonSchema {
    private JsonSchemaContext context;  // todo final

    private final JsonPointer pointer;
    private final boolean value;

    public JsonSchemaBoolean (Boolean value, JsonSchemaContext context) {
        this.context = context;
        this.pointer = JsonPointer.EMPTY;
        this.value = value;
    }

    public JsonSchemaBoolean (Boolean value) {
        this.pointer = JsonPointer.EMPTY;
        this.value = value;
    }

    public JsonSchemaBoolean (JsonPointer pointer, Boolean value) {
        this.pointer = pointer;
        this.value = value;
    }

    @Override
    public boolean getBoolean () {
        return value;
    }

    @Override
    public boolean isTrue () {
        return value;
    }

    @Override
    public boolean isFalse () {
        return !value;
    }
}

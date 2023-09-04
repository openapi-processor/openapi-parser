/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.schema;

public class JsonSchemaBoolean implements JsonSchema {
    private final JsonSchemaContext context;
    private final JsonPointer pointer;
    private final boolean value;

    public JsonSchemaBoolean (Boolean value, JsonSchemaContext context) {
        this.context = context;
        this.pointer = JsonPointer.empty();
        this.value = value;
    }

    public JsonSchemaBoolean (JsonPointer pointer, Boolean value, JsonSchemaContext context) {
        this.context = context;
        this.pointer = pointer;
        this.value = value;
    }

    @Override
    public JsonSchemaContext getContext () {
        return context;
    }

    @Override
    public JsonPointer getLocation () {
        return pointer;
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

    @Override
    public String toString () {
        return String.format ("%s (%b)", pointer.toString (), value);
    }
}

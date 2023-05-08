/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.converter;

import io.openapiprocessor.jsonschema.schema.*;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

/**
 * converts the property {@code value} to a {@link JsonSchema}. The context is the {@code
 * parentContext} or a new context if the schema has an {@code id}.
 */
public class JsonSchemaConverter implements PropertyConverter<JsonSchema> {
    private final JsonSchemaContext parentContext;

    public JsonSchemaConverter (JsonSchemaContext parentContext) {
        this.parentContext = parentContext;
    }

    @Override
    public @Nullable JsonSchema convert (String name, @Nullable Object value, String location) {
        if (value == null)
            return null;

        if (Types.isBoolean (value)) {
            return new JsonSchemaBoolean (JsonPointer.from (location), (Boolean) value, parentContext);

        } else if (Types.isObject (value)) {
            Map<String, Object> props = Types.asObject (value);
            return new JsonSchemaObject (JsonPointer.from (location), props, parentContext.withId (props));
        } else {
            throw new TypeMismatchException (location, JsonSchema.class);
        }
    }
}

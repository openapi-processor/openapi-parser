/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.converter;

import io.openapiparser.schema.*;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

import static io.openapiparser.converter.Types.asMap;
import static io.openapiparser.schema.JsonPointer.from;

/**
 * converts the property {@code value} to a {@link JsonSchema}.
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
        if (value instanceof Boolean) {
            return new JsonSchemaBoolean (from (location), (Boolean) value, parentContext);

        } else if (value instanceof Map) {
            Map<String, Object> props = asMap (value);
            return new JsonSchemaObject (from (location), props, parentContext.withId (props));
        } else {
            throw new TypeMismatchException (location, JsonSchema.class);
        }
    }
}

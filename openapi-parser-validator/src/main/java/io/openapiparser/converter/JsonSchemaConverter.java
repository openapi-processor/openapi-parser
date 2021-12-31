/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.converter;

import io.openapiparser.schema.*;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

/**
 * converts property {@code value} to {@link JsonSchema} object.
 */
public class JsonSchemaConverter implements PropertyConverter<JsonSchema> {

    @Override
    public @Nullable JsonSchema convert (String name, Object value, String location) {
        if (value == null)
            return null;

        if (value instanceof Boolean) {
            return new JsonSchemaBoolean (JsonPointer.fromJsonPointer (location), (Boolean) value);

        } else if (value instanceof Map) {
            //noinspection unchecked
            return new JsonSchemaObject (JsonPointer.fromJsonPointer (location), (Map<String, Object>) value);
        } else {
            throw new TypeMismatchException (location, JsonSchema.class);
        }
    }
}

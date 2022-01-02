/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.converter;

import io.openapiparser.schema.*;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

import static io.openapiparser.converter.Types.asMap;
import static io.openapiparser.schema.JsonPointer.fromJsonPointer;

/**
 * converts the property {@code value} to a {@link JsonSchema}.
 */
public class JsonSchemaConverter implements PropertyConverter<JsonSchema> {

    @Override
    public @Nullable JsonSchema convert (String name, Object value, String location) {
        if (value == null)
            return null;

        if (value instanceof Boolean) {
            return new JsonSchemaBoolean (fromJsonPointer (location), (Boolean) value);

        } else if (value instanceof Map) {
            return new JsonSchemaObject (fromJsonPointer (location), asMap (value));
        } else {
            throw new TypeMismatchException (location, JsonSchema.class);
        }
    }
}

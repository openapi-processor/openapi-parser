/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.converter;

import io.openapiparser.schema.*;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

import static io.openapiparser.converter.Types.asMap;
import static io.openapiparser.schema.JsonPointer.from;

/**
 * converts the property {@code value} to a {@link JsonSchema}. the {@code refContext} is the final
 * context.
 */
public class JsonSchemaRefConverter implements PropertyConverter<JsonSchema> {
    private final JsonSchemaContext refContext;

    public JsonSchemaRefConverter (JsonSchemaContext refContext) {
        this.refContext = refContext;
    }

    @Override
    public @Nullable JsonSchema convert (String name, @Nullable Object value, String location) {
        if (value == null)
            return null;

        if (value instanceof Boolean) {
            return new JsonSchemaBoolean (from (location), (Boolean) value, refContext);

        } else if (value instanceof Map) {
            Map<String, Object> props = asMap (value);
            return new JsonSchemaObject (from (location), props, refContext);
        } else {
            throw new TypeMismatchException (location, JsonSchema.class);
        }
    }
}

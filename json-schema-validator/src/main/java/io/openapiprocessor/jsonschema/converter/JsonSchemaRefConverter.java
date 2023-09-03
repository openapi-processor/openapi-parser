/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.converter;

import io.openapiprocessor.jsonschema.schema.*;
import io.openapiprocessor.jsonschema.support.Types;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

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
            return new JsonSchemaBoolean (JsonPointer.from (location), (Boolean) value, refContext);

        } else if (value instanceof Map) {
            Map<String, Object> props = Types.asMap (value);
            return new JsonSchemaObject (JsonPointer.from (location), props, refContext);
        } else {
            throw new TypeMismatchException (location, JsonSchema.class);
        }
    }
}

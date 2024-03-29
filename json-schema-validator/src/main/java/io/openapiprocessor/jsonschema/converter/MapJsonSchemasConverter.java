/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.converter;

import io.openapiprocessor.jsonschema.schema.JsonPointer;
import io.openapiprocessor.jsonschema.schema.JsonSchema;
import io.openapiprocessor.jsonschema.schema.JsonSchemaContext;
import io.openapiprocessor.jsonschema.support.Types;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static io.openapiprocessor.jsonschema.support.Null.nonNull;

/**
 * get a map of {@link String} to {@link JsonSchema}.
 */
public class MapJsonSchemasConverter implements PropertyConverter<Map<String, JsonSchema>> {
    private final JsonSchemaContext parentContext;

    public MapJsonSchemasConverter (JsonSchemaContext parentContext) {
        this.parentContext = parentContext;
    }

    @Override
    public @Nullable Map<String, JsonSchema> convert (String name, @Nullable Object value, String location) {
        Map<String, Object> objects = Types.convertMapOrNull (location, value);
        if (objects == null)
            return null;

        JsonPointer parentLocation = JsonPointer.from (location);

        Map<String, JsonSchema> result = new LinkedHashMap<> ();
        objects.forEach ((propKey, propValue) -> {
            result.put (propKey, nonNull(
                create (name, propValue, getLocation (parentLocation, propKey))
            ));
        });

        return Collections.unmodifiableMap (result);
    }

    private @Nullable JsonSchema create (String name, Object value, String location) {
        return new JsonSchemaConverter (parentContext).convert (name, value, location);
    }

    private String getLocation (JsonPointer parent, String property) {
        return parent.getJsonPointer (property);
    }
}

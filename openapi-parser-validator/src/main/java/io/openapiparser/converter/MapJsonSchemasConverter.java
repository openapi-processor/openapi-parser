/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.converter;

import io.openapiparser.schema.*;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.*;

import static io.openapiparser.converter.Types.convertMapOrNull;

/**
 * get a map of {@link String} to {@link JsonSchema}.
 */
public class MapJsonSchemasConverter implements PropertyConverter<Map<String, JsonSchema>> {
    private final JsonSchemaContext parentContext;

    public MapJsonSchemasConverter (JsonSchemaContext parentContext) {
        this.parentContext = parentContext;
    }

    @Override
    public @Nullable Map<String, JsonSchema> convert (String name, Object value, String location) {
        Map<String, Object> objects = convertMapOrNull (location, value);
        if (objects == null)
            return null;

        JsonPointer parentLocation = JsonPointer.from (location);

        Map<String, JsonSchema> result = new LinkedHashMap<> ();
        objects.forEach ((propKey, propValue) -> {
            result.put (propKey, create (name, propValue, getLocation(parentLocation, propKey)));
        });

        return Collections.unmodifiableMap (result);
    }

    private JsonSchema create (String name, Object value, String location) {
        return new JsonSchemaConverter (parentContext).convert (name, value, location);
    }

    private String getLocation (JsonPointer parent, String property) {
        return parent.getJsonPointer (property);
    }
}

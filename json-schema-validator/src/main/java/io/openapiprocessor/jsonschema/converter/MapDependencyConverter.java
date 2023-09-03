/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.converter;

import io.openapiprocessor.jsonschema.schema.JsonDependency;
import io.openapiprocessor.jsonschema.schema.JsonPointer;
import io.openapiprocessor.jsonschema.schema.JsonSchema;
import io.openapiprocessor.jsonschema.schema.JsonSchemaContext;
import io.openapiprocessor.jsonschema.support.Types;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static io.openapiprocessor.jsonschema.support.Null.nonNull;

/**
 * get a map of {@link String} to {@link JsonDependency}.
 */
public class MapDependencyConverter implements PropertyConverter<Map<String, JsonDependency>> {
    private final JsonSchemaContext parentContext;

    public MapDependencyConverter (JsonSchemaContext parentContext) {
        this.parentContext = parentContext;
    }

    @Override
    public @Nullable Map<String, JsonDependency> convert (
        String name, @Nullable Object value, String location)
    {
        Map<String, Object> objects = Types.convertMapOrNull (location, value);
        if (objects == null)
            return null;

        JsonPointer parentLocation = JsonPointer.from (location);

        Map<String, JsonDependency> result = new LinkedHashMap<> ();
        objects.forEach ((propKey, propValue) -> {
            if (Types.isObject(propValue) || Types.isBoolean (propValue)) {
                result.put (propKey, new JsonDependency (
                    create (name, propValue, getLocation (parentLocation, propKey)))
                );
            } else if (Types.isArray (propValue)) {
                Collection<String> property = Types.asCol (propValue);
                result.put (propKey, new JsonDependency (property));
            }
        });

        return Collections.unmodifiableMap (result);
    }

    private JsonSchema create (String name, Object value, String location) {
        return nonNull (new JsonSchemaConverter (parentContext).convert (name, value, location));
    }

    private String getLocation (JsonPointer parent, String property) {
        return parent.getJsonPointer (property);
    }
}

/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.converter;

import io.openapiparser.schema.*;
import org.checkerframework.checker.nullness.qual.*;

import java.util.*;

import static io.openapiparser.converter.Types.*;
import static io.openapiparser.support.Nullness.nonNull;

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
        Map<String, Object> objects = convertMapOrNull (location, value);
        if (objects == null)
            return null;

        JsonPointer parentLocation = JsonPointer.from (location);

        Map<String, JsonDependency> result = new LinkedHashMap<> ();
        objects.forEach ((propKey, propValue) -> {
            if (isObject(propValue) || isBoolean (propValue)) {
                result.put (propKey, new JsonDependency (
                    create (name, propValue, getLocation (parentLocation, propKey)))
                );
            } else if (isArray (propValue)) {
                Collection<String> property = asCol (propValue);
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

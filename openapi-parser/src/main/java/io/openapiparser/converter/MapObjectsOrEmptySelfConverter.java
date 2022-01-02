/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.converter;

import io.openapiparser.Context;
import io.openapiparser.schema.JsonPointer;

import java.util.*;

/**
 * get a map of {@link T}s.
 */
public class MapObjectsOrEmptySelfConverter<T> implements PropertiesConverter<Map<String, T>> {
    private final Context context;
    private final Class<T> object;

    public MapObjectsOrEmptySelfConverter (Context context, Class<T> object) {
        this.context = context;
        this.object = object;
    }

    @Override
    public Map<String, T> convert (Map<String, Object> properties, String location) {
        ObjectNullableConverter<T> converter = new ObjectNotNullConverter<> (context, object);

        Map<String, T> objects = new LinkedHashMap<> ();

        properties.forEach ((property, value) -> {
            String propertyLocation = getLocation (location, property);
            T propertyObject = converter.convert (property, value, propertyLocation);
            objects.put (property, propertyObject);
        });

        return Collections.unmodifiableMap (objects);
    }

    private String getLocation (String location, String property) {
        return JsonPointer.fromJsonPointer (location).getJsonPointer (property);
    }
}

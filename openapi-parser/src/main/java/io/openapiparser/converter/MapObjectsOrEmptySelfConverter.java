/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.converter;

import io.openapiparser.Factory;
import io.openapiparser.schema.JsonPointer;

import java.net.URI;
import java.util.*;

/**
 * get a map of {@link T}s.
 */
public class MapObjectsOrEmptySelfConverter<T> implements PropertiesConverter<Map<String, T>> {
    private final URI uri;
    private final Factory<T> factory;
    private ObjectNullableConverter<T> converter;

    public MapObjectsOrEmptySelfConverter (URI uri, Factory<T> factory) {
        this.uri = uri;
        this.factory = factory;
        this.converter = new ObjectNotNullConverter<> (uri, factory);

    }

    @Override
    public Map<String, T> convert (Map<String, Object> properties, String location) {
        Map<String, T> objects = new LinkedHashMap<> ();

        properties.forEach ((property, value) -> {
            objects.put (property, create (property, value, getLocation (location, property)));
        });

        return Collections.unmodifiableMap (objects);
    }

    private T create (String name, Object item, String location) {
        return converter.convert (name, item, location);
    }

    private String getLocation (String location, String property) {
        return JsonPointer.fromJsonPointer (location).getJsonPointer (property);
    }
}

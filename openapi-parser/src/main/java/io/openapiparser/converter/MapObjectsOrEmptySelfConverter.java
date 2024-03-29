/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.converter;

import io.openapiparser.Factory;
import io.openapiprocessor.jsonschema.converter.PropertiesConverter;
import io.openapiprocessor.jsonschema.schema.JsonPointer;
import io.openapiprocessor.jsonschema.schema.Scope;

import java.util.*;

import static io.openapiprocessor.jsonschema.support.Null.nonNull;

/**
 * get a map of {@link T}s.
 */
public class MapObjectsOrEmptySelfConverter<T> implements PropertiesConverter<Map<String, T>> {
    private final ObjectNotNullConverter<T> converter;

//    public MapObjectsOrEmptySelfConverter (URI uri, Factory<T> factory) {
//        this.converter = new ObjectNotNullConverter<> (uri, factory);
//    }

    public MapObjectsOrEmptySelfConverter (Scope scope, Factory<T> factory) {
        this.converter = new ObjectNotNullConverter<> (scope, factory);
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
        return nonNull (converter.convert (name, item, location));
    }

    private String getLocation (String location, String property) {
        return JsonPointer.from (location).getJsonPointer (property);
    }
}

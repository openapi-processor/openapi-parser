/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.converter;

import io.openapiparser.Context;
import io.openapiparser.schema.JsonPointer;

import java.util.*;

import static io.openapiparser.converter.Types.convertOrNull;

/**
 * get a collection of {@link T}s.
 */
public class ObjectsOrEmptyConverter<T> implements PropertyConverter<Collection<T>> {
    private final Context context;
    private final Class<T> object;

    public ObjectsOrEmptyConverter (Context context, Class<T> object) {
        this.context = context;
        this.object = object;
    }

    @Override
    public Collection<T> convert (String name, Object value, String location) {
        Collection<?> objects = convertOrNull (location, value, Collection.class);
        if (objects == null)
            return Collections.emptyList ();

        JsonPointer parentLocation = JsonPointer.fromJsonPointer (location);

        int index = 0;
        Collection<T> result = new ArrayList<> ();
        for (Object item : objects) {
            result.add (create (name, item, getLocation(parentLocation, index++)));
        }
        return Collections.unmodifiableCollection (result);
    }

    private T create (String name, Object value, String location) {
        return new ObjectNotNullConverter<T> (context, object).convert (name, value, location);
    }

    private String getLocation (JsonPointer parent, int index) {
        return parent.getJsonPointer (String.valueOf (index));
    }
}

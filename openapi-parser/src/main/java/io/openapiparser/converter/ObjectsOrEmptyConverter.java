/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.converter;

import io.openapiparser.Factory;
import io.openapiparser.schema.JsonPointer;

import java.net.URI;
import java.util.*;

import static io.openapiparser.converter.Types.convertOrNull;

/**
 * get a collection of {@link T}s.
 */
public class ObjectsOrEmptyConverter<T> implements PropertyConverter<Collection<T>> {
    private final URI uri;
    private final Factory<T> factory;
    private ObjectNotNullConverter<T> converter;

    public ObjectsOrEmptyConverter (URI uri, Factory<T> factory) {
        this.uri = uri;
        this.factory = factory;
        this.converter = new ObjectNotNullConverter<T> (uri, factory);
    }

    @Override
    public Collection<T> convert (String name, Object value, String location) {
        Collection<?> objects = convertOrNull (location, value, Collection.class);
        if (objects == null)
            return Collections.emptyList ();

        JsonPointer parentPointer = JsonPointer.fromJsonPointer (location);

        Collection<T> result = new ArrayList<> ();

        int index = 0;
        for (Object item : objects) {
            T itemObject = create (name, item, getIndexLocation (parentPointer, index));
            result.add (itemObject);
            index++;
        }
        return Collections.unmodifiableCollection (result);
    }

    private T create (String name, Object item, String location) {
        return converter.convert (name, item, location);
    }

    private String getIndexLocation (JsonPointer parent, int index) {
        return parent.getJsonPointer (String.valueOf (index));
    }
}

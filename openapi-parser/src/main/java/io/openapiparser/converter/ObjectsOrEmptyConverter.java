/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.converter;

import io.openapiparser.Factory;
import io.openapiparser.schema.JsonPointer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;
import java.util.*;

import static io.openapiparser.converter.Types.convertOrNull;
import static io.openapiparser.support.Nullness.nonNull;

/**
 * get a collection of {@link T}s.
 */
public class ObjectsOrEmptyConverter<T> implements PropertyConverter<Collection<T>> {
    private final ObjectNotNullConverter<T> converter;

    public ObjectsOrEmptyConverter (URI uri, Factory<T> factory) {
        this.converter = new ObjectNotNullConverter<T> (uri, factory);
    }

    @Override
    public @Nullable Collection<T> convert (String name, @Nullable Object value, String location) {
        Collection<@NonNull ?> objects = convertOrNull (location, value, Collection.class);
        if (objects == null)
            return Collections.emptyList ();

        JsonPointer parentPointer = JsonPointer.from (location);

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
        return nonNull (converter.convert (name, item, location));
    }

    private String getIndexLocation (JsonPointer parent, int index) {
        return parent.getJsonPointer (String.valueOf (index));
    }
}

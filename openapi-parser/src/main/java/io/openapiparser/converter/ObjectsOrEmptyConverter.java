/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.converter;

import io.openapiparser.Factory;
import io.openapiprocessor.jsonschema.converter.PropertyConverter;
import io.openapiprocessor.jsonschema.schema.JsonPointer;
import io.openapiprocessor.jsonschema.schema.Scope;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.*;

import static io.openapiprocessor.jsonschema.support.Types.convertOrNull;
import static io.openapiprocessor.jsonschema.support.Nullness.nonNull;

/**
 * get a collection of {@link T}s.
 */
public class ObjectsOrEmptyConverter<T> implements PropertyConverter<Collection<T>> {
    private final ObjectNotNullConverter<T> converter;

    public ObjectsOrEmptyConverter (Scope scope, Factory<T> factory) {
        this.converter = new ObjectNotNullConverter<T> (scope, factory);
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

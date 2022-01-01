/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.converter;

import io.openapiparser.Context;
import io.openapiparser.schema.Bucket;

import java.util.Collections;
import java.util.Map;

/**
 * get a map of {@link T}s from {@code name} property {@code value}.
 */
public class MapObjectsOrEmptyFromPropertyConverter<T> implements PropertyConverter<Map<String, T>> {
    private final Context context;
    private final Class<T> object;

    public MapObjectsOrEmptyFromPropertyConverter (Context context, Class<T> object) {
        this.context = context;
        this.object = object;
    }

    @Override
    public Map<String, T> convert (String name, Object value, String location) {
        Bucket bucket = new PropertyBucketConverter ().convert (name, value, location);
        if (bucket == null)
            return Collections.emptyMap ();

        return bucket.convert (new MapObjectsOrEmptyConverter<> (context, object));
    }
}

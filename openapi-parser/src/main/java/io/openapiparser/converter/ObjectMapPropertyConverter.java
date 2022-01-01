/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.converter;

import io.openapiparser.Context;
import io.openapiparser.schema.PropertyBucket;

import java.util.Collections;
import java.util.Map;

/**
 * get a map of {@link T}s from {@code name} property {@code value}.
 */
public class ObjectMapPropertyConverter<T> implements PropertyConverter<Map<String, T>> {
    private final Context context;
    private final Class<T> object;

    public ObjectMapPropertyConverter (Context context, Class<T> object) {
        this.context = context;
        this.object = object;
    }

    @Override
    public Map<String, T> convert (String name, Object value, String location) {
        PropertyBucket bucket = new PropertyBucketConverter ().convert (name, value, location);
        if (bucket == null)
            return Collections.emptyMap ();

        return bucket.convert (new ObjectMapConverter<> (context, object));
    }
}
/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.converter;

import io.openapiparser.Context;
import io.openapiparser.schema.PropertyBucket;

/**
 *  get a {@link T} object from the property.
 */
public class ObjectConverter<T> implements PropertyConverter<T> {
    private final Context context;
    private final Class<T> object;

    public ObjectConverter (Context context, Class<T> object) {
        this.context = context;
        this.object = object;
    }

    @Override
    public T convert (String name, Object value, String location) {
        PropertyBucket bucket = new PropertyBucketConverter ().convert (name, value, location);
        if (bucket == null)
            return null;

        return create (context, bucket);
    }

    private T create (Context context, PropertyBucket bucket) {
        try {
            return object.getDeclaredConstructor (Context.class, PropertyBucket.class)
                .newInstance (context, bucket);
        } catch (Exception e) {
            throw new RuntimeException (String.format("failed to create %s", object.getName ()), e);
        }
    }
}

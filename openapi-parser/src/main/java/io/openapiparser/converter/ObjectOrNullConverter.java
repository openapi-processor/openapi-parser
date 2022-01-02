/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.converter;

import io.openapiparser.Context;
import io.openapiparser.schema.Bucket;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 *  get a {@link T} object from the property.
 */
public class ObjectOrNullConverter<T> implements PropertyConverter<T> {
    private final Context context;
    private final Class<T> object;

    public ObjectOrNullConverter (Context context, Class<T> object) {
        this.context = context;
        this.object = object;
    }

    @Override
    public @Nullable T convert (String name, Object value, String location) {
        Bucket bucket = new BucketConverter ().convert (name, value, location);
        if (bucket == null)
            return null;

        return create (context, bucket);
    }

    private T create (Context context, Bucket bucket) {
        try {
            return object.getDeclaredConstructor (Context.class, Bucket.class)
                .newInstance (context, bucket);
        } catch (Exception e) {
            throw new RuntimeException (String.format("failed to create %s", object.getName ()), e);
        }
    }
}

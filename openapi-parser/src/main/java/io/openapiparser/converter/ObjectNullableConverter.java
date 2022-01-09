/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.converter;

import io.openapiparser.Factory;
import io.openapiparser.schema.Bucket;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;
import java.util.Map;

import static io.openapiparser.converter.Types.asMap;

/**
 *  get a {@link T} object from the property.
 */
public class ObjectNullableConverter<T> implements PropertyConverter<T> {
    private final URI uri;
    private final Factory<T> factory;

    public ObjectNullableConverter (URI uri, Factory<T> factory) {
        this.uri = uri;
        this.factory = factory;
    }

    @Override
    public @Nullable T convert (String name, Object value, String location) {
        Bucket bucket = getBucket (value, location);
        if (bucket == null)
            return null;

        return factory.create (bucket);
    }

    private @Nullable Bucket getBucket (Object value, String location) {
        if (value == null)
            return null;

        if (!(value instanceof Map))
            throw new TypeMismatchException (location, Map.class);

        return new Bucket (uri, location, asMap (value));
    }
}

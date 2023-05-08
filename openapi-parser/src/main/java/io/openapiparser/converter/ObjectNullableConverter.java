/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.converter;

import io.openapiparser.Factory;
import io.openapiprocessor.jsonschema.converter.PropertyConverter;
import io.openapiprocessor.jsonschema.converter.TypeMismatchException;
import io.openapiprocessor.jsonschema.schema.Bucket;
import io.openapiprocessor.jsonschema.schema.Scope;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

import static io.openapiprocessor.jsonschema.converter.Types.asMap;
import static io.openapiprocessor.jsonschema.converter.Types.isMap;

/**
 *  get a {@link T} object from the property.
 */
public class ObjectNullableConverter<T> implements PropertyConverter<T> {
    private final Scope scope;
    private final Factory<T> factory;

    public ObjectNullableConverter (Scope scope, Factory<T> factory) {
        this.scope = scope;
        this.factory = factory;
    }

    @Override
    public @Nullable T convert (String name, @Nullable Object value, String location) {
        Bucket bucket = getBucket (value, location);
        if (bucket == null)
            return null;

        return factory.create (bucket);
    }

    private @Nullable Bucket getBucket (@Nullable Object value, String location) {
        if (value == null)
            return null;

        if (!isMap (value))
            throw new TypeMismatchException (location, Map.class);

        return new Bucket (scope, location, asMap (value));
    }
}

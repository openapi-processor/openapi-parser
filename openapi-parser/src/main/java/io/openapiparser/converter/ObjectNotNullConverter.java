/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.converter;

import io.openapiparser.Factory;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;

/**
 *  get a {@link T} object from the property or throw if the property has no value.
 */
public class ObjectNotNullConverter<T> extends ObjectNullableConverter<T> {

    public ObjectNotNullConverter (URI uri, Factory<T> factory) {
        super (uri, factory);
    }

    @Override
    public @Nullable T convert (String name, @Nullable Object value, String location) {
        final T object = super.convert (name, value, location);
        if (object == null)
            throw new NoValueException (location);

        return object;
    }
}


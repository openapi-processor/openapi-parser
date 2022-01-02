/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.converter;

import io.openapiparser.Context;

/**
 *  get a {@link T} object from the property or throw if the property has no value.
 */
public class ObjectOrThrowConverter<T> extends ObjectOrNullConverter<T> {

    public ObjectOrThrowConverter (Context context, Class<T> object) {
        super (context, object);
    }

    @Override
    public T convert (String name, Object value, String location) {
        final T object = super.convert (name, value, location);
        if (object == null)
            throw new NoValueException (location);

        return object;
    }
}


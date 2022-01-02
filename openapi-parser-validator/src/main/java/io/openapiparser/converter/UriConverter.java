/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.converter;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;

/**
 * converts the property {@code value} to an {@link URI}.
 */
public class UriConverter implements PropertyConverter<URI> {

    @Override
    public @Nullable URI convert (String name, Object value, String location) {
        if (value == null)
            return null;

        if (!(value instanceof String))
            throw new TypeMismatchException (location, String.class);

        return URI.create ((String)value);
    }
}

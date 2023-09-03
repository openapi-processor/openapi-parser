/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.converter;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;

import static io.openapiprocessor.jsonschema.support.Types.isString;
import static io.openapiprocessor.jsonschema.support.UriSupport.createUri;

/**
 * converts the property {@code value} to an {@link URI}.
 */
public class UriConverter implements PropertyConverter<URI> {

    @Override
    public @Nullable URI convert (String name, @Nullable Object value, String location) {
        if (value == null)
            return null;

        if (!isString (value))
            throw new TypeMismatchException (location, String.class);

        return createUri ((String)value);
    }
}

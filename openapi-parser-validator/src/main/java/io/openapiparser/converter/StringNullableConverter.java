/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.converter;

import org.checkerframework.checker.nullness.qual.Nullable;

import static io.openapiparser.converter.Types.convertOrNull;

/**
 * converts the property {@code value} to {@link String}. May be null.
 */
public class StringNullableConverter implements PropertyConverter<String> {

    @Override
    public @Nullable String convert (String name, @Nullable Object value, String location) {
        return convertOrNull (location, value, String.class);
    }
}

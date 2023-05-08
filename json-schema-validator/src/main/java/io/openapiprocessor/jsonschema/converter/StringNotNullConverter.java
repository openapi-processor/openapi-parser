/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.converter;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * converts the property {@code value} to {@link String} and throws {@link NoValueException} if the
 * value is {@code null}.
 */
public class StringNotNullConverter implements PropertyConverter<String> {

    @Override
    public String convert (String name, @Nullable Object value, String location) {
        final String result = Types.convertOrNull (location, value, String.class);
        if (result == null)
            throw new NoValueException (location);

        return result;
    }
}

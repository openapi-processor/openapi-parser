/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.converter;

import org.checkerframework.checker.nullness.qual.Nullable;

import static io.openapiprocessor.jsonschema.converter.Types.convertOrNull;

/**
 * converts the property {@code value} to {@link Integer}.
 */
public class IntegerConverter implements PropertyConverter<Integer> {

    @Override
    public @Nullable Integer convert (String name, @Nullable Object value, String location) {
        Number number = convertOrNull(location, value, Number.class);
        if (number == null)
            return null;

        return number.intValue();
    }
}

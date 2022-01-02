/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.converter;

import org.checkerframework.checker.nullness.qual.Nullable;

import static io.openapiparser.converter.Types.convertOrNull;

/**
 * converts the property {@code value} to {@link Integer}.
 */
public class IntegerConverter implements PropertyConverter<Integer> {

    @Override
    public @Nullable Integer convert (String name, Object value, String location) {
        return convertOrNull (location, value, Integer.class);
    }
}

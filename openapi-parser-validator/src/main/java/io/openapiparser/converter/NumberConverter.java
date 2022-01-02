/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.converter;

import org.checkerframework.checker.nullness.qual.Nullable;

import static io.openapiparser.converter.Types.convertOrNull;

/**
 * converts property {@code value} to {@link Number} object.
 */
public class NumberConverter implements PropertyConverter<Number> {

    @Override
    public @Nullable Number convert (String name, Object value, String location) {
        return convertOrNull (location, value, Number.class);
    }
}

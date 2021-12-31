/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.converter;

import org.checkerframework.checker.nullness.qual.Nullable;

import static io.openapiparser.converter.Types.convertOrNull;

/**
 * converts property {@code value} to {@link Boolean} object.
 */
public class BooleanConverter implements PropertyConverter<Boolean> {

    @Override
    public @Nullable Boolean convert (String name, Object value, String location) {
        return convertOrNull (location, value, Boolean.class);
    }
}

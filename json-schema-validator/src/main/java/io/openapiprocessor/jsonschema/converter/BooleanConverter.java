/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.converter;

import org.checkerframework.checker.nullness.qual.Nullable;

import static io.openapiprocessor.jsonschema.support.Types.convertOrNull;

/**
 * converts the property {@code value} to {@link Boolean}.
 */
public class BooleanConverter implements PropertyConverter<Boolean> {

    @Override
    public @Nullable Boolean convert (String name, @Nullable Object value, String location) {
        return convertOrNull (location, value, Boolean.class);
    }
}

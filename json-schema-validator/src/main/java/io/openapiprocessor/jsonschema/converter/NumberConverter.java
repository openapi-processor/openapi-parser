/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.converter;

import org.checkerframework.checker.nullness.qual.Nullable;

import static io.openapiprocessor.jsonschema.support.Types.convertOrNull;

/**
 * converts the property {@code value} to {@link Number}.
 */
public class NumberConverter implements PropertyConverter<Number> {

    @Override
    public @Nullable Number convert (String name, @Nullable Object value, String location) {
        return convertOrNull (location, value, Number.class);
    }
}

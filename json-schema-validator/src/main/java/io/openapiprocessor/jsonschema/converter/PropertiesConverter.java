/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.converter;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

/**
 * convert a {@code Map} value to {@code T}. Primary use is to pass the all properties of the
 * <em>current</em> object to the converter.
 *
 * @param <T> target type.
 */
public interface PropertiesConverter<T> {
    /**
     * converts the map {@code value} to a {@code T} object or null. May throw if conversion fails.
     *
     * @param value property value
     * @param location property location, json pointer
     * @return T converted value
     */
    @Nullable T convert (Map<String, Object> value, String location);
}

/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.converter;

import io.openapiparser.schema.JsonPointer;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * convert {@code Object} value to {@code T}.
 *
 * @param <T> target type.
 */
public interface PropertyConverter<T> {
    /**
     * converts the {@code value} of a property to a {@code T} object or null. May throw if
     * conversion fails, e.g. if the property is required but null.
     *
     * @param name  property name
     * @param value property value
     * @param location property location, json pointer
     * @return T converted value
     */
    @Nullable T convert (String name, @Nullable Object value, String location);

    default @Nullable T convert (String name, @Nullable Object value, JsonPointer location) {
        return convert (name, value, location.toString ());
    }
}

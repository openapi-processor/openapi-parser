/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.converter;

/**
 * convert {@code Object} value to {@code T}.
 *
 * @param <T> target type.
 */
// PropertyConverter
public interface TypeConverter<T> {
    /**
     * converts the value of a property name & value pair to a {@code T} object or null. May throw
     * if the value doesn't fulfill the expected conditions, e.g. if it is required but null.
     *
     * @param name  property name
     * @param value property value
     * @return T converted value
     */
    @Deprecated
    T convert (String name, Object value);

    // todo remove default
    default T convert (String name, Object value, String location) {
        return convert (name, value);
    }
}

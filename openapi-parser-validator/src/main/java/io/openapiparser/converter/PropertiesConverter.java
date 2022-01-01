/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.converter;

import java.util.Map;

/**
 * convert {@code Map} value to {@code T}.
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
    T convert (Map<String, Object> value, String location);
}

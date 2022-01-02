/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.converter;

import java.util.Collections;
import java.util.Map;

/**
 * get a map of {@link String}s from {@code name} property {@code value}.
 */
public class MapStringsOrEmptyConverter implements PropertyConverter<Map<String, String>> {

    @Override
    public Map<String, String> convert (String name, Object value, String location) {
        final Map<String, Object> values = Types.convertMapOrNull (location, value);
        if (value == null)
            return Collections.emptyMap ();

        return Collections.unmodifiableMap (asMap(values));
    }

    @SuppressWarnings ("unchecked")
    private static Map<String, String> asMap (Object value) {
        return (Map<String, String>) value;
    }
}

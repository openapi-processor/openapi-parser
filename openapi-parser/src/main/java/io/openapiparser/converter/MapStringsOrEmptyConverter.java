/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.converter;

import io.openapiprocessor.jsonschema.converter.PropertyConverter;
import io.openapiprocessor.jsonschema.converter.Types;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collections;
import java.util.Map;

import static io.openapiprocessor.jsonschema.support.Nullness.nonNull;

/**
 * get a map of {@link String}s from {@code name} property {@code value}.
 */
public class MapStringsOrEmptyConverter implements PropertyConverter<Map<String, String>> {

    @Override
    public @Nullable Map<String, String> convert (String name, @Nullable Object value, String location) {
        final Map<String, Object> values = Types.convertMapOrNull (location, value);
        if (value == null)
            return Collections.emptyMap ();

        return Collections.unmodifiableMap (asMap(values));
    }

    @SuppressWarnings ({"unchecked", "rawtypes"})
    private static Map<String, String> asMap (@Nullable Map<String, Object> value) {
        return (Map<String, String>) nonNull ((Map) value);
    }
}

/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.converter;

import io.openapiparser.Factory;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;
import java.util.Collections;
import java.util.Map;

import static io.openapiparser.converter.Types.convertMapOrNull;

/**
 * get a map of {@link T}s from {@code name} property {@code value}.
 */
public class MapObjectsOrEmptyConverter<T> implements PropertyConverter<Map<String, T>> {
    private final MapObjectsOrEmptySelfConverter<T> converter;

    public MapObjectsOrEmptyConverter (URI uri, Factory<T> factory) {
        this.converter = new MapObjectsOrEmptySelfConverter<> (uri, factory);
    }

    @Override
    public @Nullable Map<String, T> convert (String name,@Nullable Object value, String location) {
        Map<String, Object> properties = convertMapOrNull (location, value);
        if (properties == null)
            return Collections.emptyMap ();

        return converter.convert (properties, location);
    }
}

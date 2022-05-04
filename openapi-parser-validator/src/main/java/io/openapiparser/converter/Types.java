/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.converter;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;
import java.util.Map;

/**
 * type conversion/cast utility functions.
 */
public class Types {

    @SuppressWarnings ("unchecked")
    public static <T> T convert (String path, @Nullable Object value, Class<T> type) {
        if (!type.isInstance (value))
            throw new TypeMismatchException (path, type);

        return (T) value;
    }

    public static <T> @Nullable T convertOrNull (String path, @Nullable Object value, Class<T> type) {
        if (value == null)
            return null;

        return convert (path, value, type);
    }

    static <T> T convertOrFallback (String path, @Nullable Object value, Class<T> type, T fallback) {
        if (value == null)
            return fallback;

        return convert (path, value, type);
    }

    public static <T> T convertNotNull (String path, @Nullable Object value, Class<T> type) {
        final T result = convertOrNull (path, value, type);
        if (result == null)
            throw new NoValueException (path);

        return result;
    }

    @SuppressWarnings ("unchecked")
    static Collection<Object> convertCollection (String path, @Nullable Object value) {
        return convert (path, value, Collection.class);
    }

    @SuppressWarnings ("unchecked")
    static Map<String, Object> convertMap (String path, @Nullable Object value) {
        return convert (path, value, Map.class);
    }

    @SuppressWarnings ("unchecked")
    public static @Nullable Map<String, Object> convertMapOrNull (String path, @Nullable Object value) {
        return convertOrNull (path, value, Map.class);
    }

    @SuppressWarnings ("unchecked")
    public static <T> @Nullable T as (@Nullable Object o) {
        return (T) o;
    }

    @SuppressWarnings ("unchecked")
    public static @Nullable Map<String, Object> asMap (@Nullable Object o) {
        return (Map<String, Object>) o;
    }

    @SuppressWarnings ("unchecked")
    public static <T> @Nullable Collection<T> asCol (@Nullable Object o) {
        return (Collection<T>) o;
    }

    public static boolean isString (@Nullable Object o) {
        return o instanceof String;
    }
}

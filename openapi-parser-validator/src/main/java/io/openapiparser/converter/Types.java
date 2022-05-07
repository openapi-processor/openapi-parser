/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.converter;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.checker.nullness.qual.PolyNull;

import java.util.Collection;
import java.util.Map;

/**
 * type conversion/cast utility functions.
 *
 * todo move to support package
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
    public static @PolyNull Map<String, Object> asMap (@PolyNull Object o) {
        return (Map<String, Object>) o;
    }

    @SuppressWarnings ("unchecked")
    public static <T> @PolyNull Collection<T> asCol (@PolyNull Object o) {
        return (Collection<T>) o;
    }

    public static @PolyNull String asString (@PolyNull Object o) {
        return (String) o;
    }

    public static @PolyNull Number asNumber (@PolyNull Object o) {
        return (Number) o;
    }

    public static boolean isBoolean (@Nullable Object o) {
        return o instanceof Boolean;
    }

    public static boolean isString (@Nullable Object o) {
        return o instanceof String;
    }

    public static boolean isObject (@Nullable Object o) {
        return o instanceof Map;
    }

    public static boolean isArray (@Nullable Object o) {
        return o instanceof Collection;
    }

    public static boolean isSchema (@Nullable Object o) {
        return isObject (o) || isBoolean (o);
    }
}

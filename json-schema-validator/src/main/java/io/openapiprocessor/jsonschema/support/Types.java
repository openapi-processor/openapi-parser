/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.support;

import io.openapiprocessor.jsonschema.converter.TypeMismatchException;
import org.checkerframework.checker.nullness.qual.*;

import java.util.Collection;
import java.util.Map;

/**
 * type conversion/cast utility functions.
 */
public class Types {

    private Types() {}

    @SuppressWarnings ("unchecked")
    public static <T> T convert (String path, @Nullable Object value, Class<T> type) {
        if (!type.isInstance (value))
            throw new TypeMismatchException(path, type);

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
    public static Collection<Object> convertCollection (String path, @Nullable Object value) {
        return convert (path, value, Collection.class);
    }

    @SuppressWarnings ("unchecked")
    public static Map<String, @Nullable Object> convertMap (String path, @Nullable Object value) {
        return convert (path, value, Map.class);
    }

    @SuppressWarnings ("unchecked")
    public static @Nullable Map<String, @Nullable Object> convertMapOrNull (String path, @Nullable Object value) {
        return convertOrNull (path, value, Map.class);
    }

    @SuppressWarnings ("unchecked")
    public static @PolyNull Map<String, @Nullable Object> asMap (@PolyNull Object o) {
        return (Map<String, @Nullable Object>) o;
    }

    @SuppressWarnings ("unchecked")
    public static @PolyNull Map<String, @Nullable Object> asObject (@PolyNull Object o) {
        return (Map<String, @Nullable Object>) o;
    }

    @SuppressWarnings ("unchecked")
    public static <T> @PolyNull Collection<@Nullable T> asCol (@PolyNull Object o) {
        return (Collection<@Nullable T>) o;
    }

    /**
     * Returns {@code o} as {@code @Nullable Map<String, @Nullable Object>} or null if it not a map.
     * @param o the potential map
     * @return the map or null if {@code o} is not a map
     */
    public static @Nullable Map<String, @Nullable Object> asObjectOrNull(@PolyNull Object o) {
        if (!isObject(o))
            return null;

        return asObject(o);
    }

    @SuppressWarnings("unchecked")
    public static <T> @PolyNull Collection<T> asColNonNull(@PolyNull Object o) {
        return (Collection<T>) o;
    }

    @SuppressWarnings ("unchecked")
    public static <T> @PolyNull Collection<T> asArray (@PolyNull Object o) {
        return (Collection<T>) o;
    }

    public static @PolyNull String asString (@PolyNull Object o) {
        return (String) o;
    }

    public static @PolyNull Number asNumber (@PolyNull Object o) {
        return (Number) o;
    }

    public static @PolyNull Boolean asBoolean (@PolyNull Object o) {
        return (Boolean) o;
    }

    @EnsuresNonNullIf(expression = "#1", result = true)
    public static boolean isBoolean (@Nullable Object o) {
        return o instanceof Boolean;
    }

    @EnsuresNonNullIf(expression = "#1", result = true)
    public static boolean isString (@Nullable Object o) {
        return o instanceof String;
    }

    @EnsuresNonNullIf(expression = "#1", result = true)
    public static boolean isNumber (@Nullable Object o) {
        return o instanceof Number;
    }

    @EnsuresNonNullIf(expression = "#1", result = true)
    public static boolean isMap (@Nullable Object o) {
        return o instanceof Map;
    }

    @EnsuresNonNullIf(expression = "#1", result = true)
    public static boolean isObject (@Nullable Object o) {
        return o instanceof Map;
    }

    @EnsuresNonNullIf(expression = "#1", result = true)
    public static boolean isArray (@Nullable Object o) {
        return o instanceof Collection;
    }

    @EnsuresNonNullIf(expression = "#1", result = true)
    public static boolean isSchema (@Nullable Object o) {
        return isObject (o) || isBoolean (o);
    }
}

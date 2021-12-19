/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import org.checkerframework.checker.nullness.qual.Nullable;

public class Type {

    @SuppressWarnings ("unchecked")
    private static <T> T convert (String path, @Nullable Object value, Class<T> type) {
        if (!type.isInstance (value))
            throw new TypeMismatchException (path, type);

        return (T) value;
    }

    static <T> @Nullable T convertOrNull (String path, @Nullable Object value, Class<T> type) {
        if (value == null)
            return null;

        return convert (path, value, type);
    }

    static <T> T convertOrFallback (String path, @Nullable Object value, Class<T> type, T fallback) {
        if (value == null)
            return fallback;

        return convert (path, value, type);
    }

    static <T> T convertOrThrow (String path, @Nullable Object value, Class<T> type) {
        final T result = convertOrNull (path, value, type);
        if (value == null)
            throw new NoValueException (path);

        return result;
    }
}

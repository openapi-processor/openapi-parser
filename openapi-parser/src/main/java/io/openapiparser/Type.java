/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import io.openapiparser.converter.NoValueException;
import io.openapiparser.converter.TypeMismatchException;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.*;

import static java.util.Collections.unmodifiableCollection;
import static java.util.Collections.unmodifiableMap;

@Deprecated
public class Type {

    @FunctionalInterface
    public interface Factory<R> {
        /**
         * create an {@code R} of the {@link Object}.
         *
         * @param obj the source {@link Object}
         * @return the result
         */
        R create (int idx, Object obj);
    }

    @SuppressWarnings ("unchecked")
    static <T> T convert (String path, @Nullable Object value, Class<T> type) {
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

    @SuppressWarnings ("return")
    public static <T> T convertOrThrow (String path, @Nullable Object value, Class<T> type) {
        final T result = convertOrNull (path, value, type);
        if (value == null)
            throw new NoValueException (path);

        return result;
    }

    @SuppressWarnings ("unchecked")
    static Map<String, Object> convertMap (String path, @Nullable Object value) {
        return convert (path, value, Map.class);
    }

    @SuppressWarnings ("unchecked")
    static Map<String, Object> convertMapOrNull (String path, @Nullable Object value) {
        return convertOrNull (path, value, Map.class);
    }

    @SuppressWarnings ("unchecked")
    static <T> Map<String, T> convertMap (String path, @Nullable Object value, Class<T> itemType) {
        final Map<String, ?> map = convertMap (path, value);

        for (Map.Entry<String, ?> entry : map.entrySet ()) {
            convertOrThrow (String.format ("%s[%s]", path, entry.getKey ()), entry.getValue (), itemType);
        }

        return (Map<String, T>) unmodifiableMap (map);
    }

    @SuppressWarnings ("unchecked")
    static <T> Collection<T> convertCollection (String path, @Nullable Object value, Class<T> itemType) {
        final Collection<?> collection = convert (path, value, Collection.class);

        int idx = 0;
        for (Object item : collection) {
            convertOrThrow (String.format ("%s[%d]", path, idx++), item, itemType);
        }

        return (Collection<T>) unmodifiableCollection (collection);
    }

    static <T> @Nullable Collection<T> convertCollectionOrNull (String path, @Nullable Object value, Class<T> itemType) {
        if (value == null)
            return null;

        return convertCollection (path, value, itemType);
    }

    static <T> Collection<T> convertCollectionOrEmpty (String path, @Nullable Object value, Class<T> itemType) {
        if (value == null)
            return Collections.emptyList ();

        return convertCollection (path, value, itemType);
    }

    static <T> Collection<T> convertCollection (String path, @Nullable Object value, Factory<T> factory) {
        final Collection<?> collection = convert (path, value, Collection.class);

        Collection<T> result = new ArrayList<> ();
        int idx = 0;
        for (Object item : collection) {
            result.add (factory.create (idx++, item));
        }

        return unmodifiableCollection (result);
    }

}

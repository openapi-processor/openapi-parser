/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.support;

import org.checkerframework.checker.nullness.qual.*;

/**
 * based on org/checkerframework/checker/nullness/util/NullnessUtil.java
 */

@SuppressWarnings({"nullness", "cast"})
public class Null {

    private Null() {}

    /**
     * suppress nullable warning for a value that is never null.
     *
     * @param value the non-null value
     * @return {@code value}
     * @param <T> the type of {@code value}
     */
    @EnsuresNonNull (value = "#1")
    public static <T extends @Nullable Object> @NonNull T nonNull (@Nullable T value) {
        if (value == null) {
            throw new NullPointerException();
        }

        return (@NonNull T) value;
    }

    /**
     * suppress nullable warning for a value that can be null.
     *
     * @param value the value that may be null
     * @return {@code value}
     * @param <T> the type of {@code value}
     */
    @EnsuresNonNull (value = "#1")
    public static <T> T nullable (@Nullable T value) {
        return (@NonNull T) value;
    }
}

/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.support;

import org.checkerframework.checker.nullness.qual.*;

/**
 * based on org/checkerframework/checker/nullness/util/NullnessUtil.java
 */
public class Null {

    private Null() {}

    @SuppressWarnings({"nullness"})
    @EnsuresNonNull (value = "#1")
    public static <T> @NonNull T nonNull (@Nullable T value) {
        assert value != null : "must not be null!";
        return value;
    }

    /**
     * suppress nullable warning for values that may be null.
     *
     * @param value the value that may be null
     * @return {@code value}
     * @param <T> the type of {@code value}
     */
    @SuppressWarnings({"nullness"})
    @EnsuresNonNull (value = "#1")
    public static <T> T nullable (@Nullable T value) {
        return value;
    }
}

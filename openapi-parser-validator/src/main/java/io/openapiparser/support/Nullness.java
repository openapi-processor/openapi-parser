/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.support;

import org.checkerframework.checker.nullness.qual.*;

/**
 * based on org/checkerframework/checker/nullness/util/NullnessUtil.java
 */
public class Nullness {
    @SuppressWarnings({"nullness"})
    @EnsuresNonNull (value = "#1")
    public static <T> @NonNull T nonNull (@Nullable T value) {
        assert value != null : "must not be null!";
        return value;
    }
}

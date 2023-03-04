/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import org.checkerframework.checker.nullness.qual.Nullable;

public class RawValue {
    private final Scope scope;
    private final @Nullable Object value;

    public RawValue (Scope scope, @Nullable Object value) {
        this.scope = scope;
        this.value = value;
    }

    public Scope getScope () {
        return scope;
    }

    public @Nullable Object getValue () {
        return value;
    }
}

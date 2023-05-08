/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.schema;

import org.checkerframework.checker.nullness.qual.Nullable;

public class RefValue {
    private final Scope scope; // value scope

    private final @Nullable Object value;

    public RefValue (Scope scope, @Nullable Object value) {
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

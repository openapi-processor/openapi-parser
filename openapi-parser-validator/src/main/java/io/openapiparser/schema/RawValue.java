/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;

public class RawValue {
    private final URI scope;
    private final @Nullable Object value;

    public RawValue (URI scope, @Nullable Object value) {
        this.scope = scope;
        this.value = value;
    }

    public URI getScope () {
        return scope;
    }

    public @Nullable Object getValue () {
        return value;
    }
}

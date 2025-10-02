/*
 * Copyright 2025 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.schema;

import org.checkerframework.checker.nullness.qual.Nullable;

public interface SchemaDetector {
    boolean isJsonSchema(JsonPointer location);

    boolean shouldWalkObject(Scope currentScope, @Nullable Object value, JsonPointer location);

    boolean shouldWalkArray(Scope currentScope, @Nullable Object value, JsonPointer location);

    boolean shouldWalkMap(Scope currentScope, @Nullable Object value, JsonPointer location);
}

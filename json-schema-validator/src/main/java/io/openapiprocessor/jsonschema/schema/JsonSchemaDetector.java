/*
 * Copyright 2025 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.schema;

import io.openapiprocessor.jsonschema.support.Types;
import org.checkerframework.checker.nullness.qual.Nullable;

class JsonSchemaDetector implements SchemaDetector {

    @Override
    public boolean isJsonSchema(JsonPointer location) {
        return true;
    }

    @Override
    public boolean shouldWalkObject(Scope scope, @Nullable Object value, JsonPointer location) {
        if (!Types.isObject(value)) {
            return false;
        }

        SchemaVersion version = scope.getVersion();
        Keyword keyword = version.getKeyword(location.tail());

        return keyword != null && keyword.isNavigable() && keyword.isSchema();
    }

    @Override
    public boolean shouldWalkArray(Scope scope, @Nullable Object value, JsonPointer location) {
        if (!Types.isArray(value)) {
            return false;
        }

        SchemaVersion version = scope.getVersion();
        Keyword keyword = version.getKeyword(location.tail());

        return keyword != null && keyword.isNavigable() && keyword.isSchemaArray();
    }

    @Override
    public boolean shouldWalkMap(Scope scope, @Nullable Object value, JsonPointer location) {
        if (!Types.isMap(value)) {
            return false;
        }

        SchemaVersion version = scope.getVersion();
        Keyword keyword = version.getKeyword(location.tail());

        return keyword != null && keyword.isNavigable() && keyword.isSchemaMap();
    }
}

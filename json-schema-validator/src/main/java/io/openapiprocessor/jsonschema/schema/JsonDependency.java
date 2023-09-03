/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.schema;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;

import static io.openapiprocessor.jsonschema.support.Null.nonNull;

/**
 * handles json schema dependency which may be a schema or an array of string.
 */
@SuppressWarnings ("NullableProblems")
public class JsonDependency {
    private enum Kind {SCHEMA, ARRAY}

    private final Kind kind;
    private final @Nullable JsonSchema schema;
    private final @Nullable Collection<String> properties;

    public JsonDependency (JsonSchema schema) {
        this.kind = Kind.SCHEMA;
        this.schema = schema;
        this.properties = null;
    }

    public JsonDependency (Collection<String> values) {
        this.kind = Kind.ARRAY;
        this.schema = null;
        this.properties = values;
    }

    public boolean isSchema () {
        return kind == Kind.SCHEMA;
    }

    public JsonSchema getSchema () {
        return nonNull(schema);
    }

    public Collection<String> getProperties () {
        return nonNull(properties);
    }
}

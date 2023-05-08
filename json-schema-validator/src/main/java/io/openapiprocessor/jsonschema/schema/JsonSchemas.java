/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.schema;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.*;
import java.util.stream.StreamSupport;

/**
 * handles json schema properties which may have no value, are null or have a single or multiple
 * values.
 */
public class JsonSchemas {
    private enum Kind {EMPTY, NULL, SINGLE, ARRAY}

    private final Kind kind;
    private final int size;
    private final Iterable<JsonSchema> schemas;

    /**
     * property has no value.
     */
    public JsonSchemas () {
        kind = Kind.EMPTY;
        schemas = Collections.emptyList ();
        size = 0;
    }

    /**
     * property has a single schema, may be null.
     *
     * @param schema a schema or null.
     */
    public JsonSchemas (@Nullable JsonSchema schema) {
        if (schema == null) {
            kind = Kind.NULL;
            schemas = Collections.emptyList ();
        } else {
            kind = Kind.SINGLE;
            schemas = Collections.singletonList (schema);
        }
        size = 1;
    }

    /**
     * property has a multiple schemas.
     *
     * @param schemas schemas.
     */
    public JsonSchemas (Iterable<JsonSchema> schemas) {
        kind = Kind.ARRAY;
        this.schemas = schemas;
        size = count (schemas);
    }

    public boolean isEmpty () {
        return kind.equals (Kind.EMPTY);
    }

    public boolean isNull () {
        return kind.equals (Kind.NULL);
    }

    public boolean isSingle () {
        return kind.equals (Kind.SINGLE);
    }

    public boolean isArray () {
        return kind.equals (Kind.ARRAY);
    }

    // protect by isSingle()
    public JsonSchema getSchema () {
        return schemas.iterator ().next ();
    }

    // protect by isArray()
    public Iterable<JsonSchema> getSchemas () {
        return schemas;
    }

    public int size () {
        return size;
    }

    private static int count (Iterable<JsonSchema> schemas) {
        return (int) StreamSupport.stream (schemas.spliterator (), false).count ();
    }
}

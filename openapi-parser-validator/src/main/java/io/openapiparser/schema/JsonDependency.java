/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import java.util.Collection;
import java.util.Collections;

/**
 * handles json schema dependency which may be a schema or an array of string.
 */
public class JsonDependency {
    private enum Kind {SCHEMA, ARRAY}

    private final Kind kind;
    private final JsonSchema schema;
    private final Collection<String> properties;

    public JsonDependency (JsonSchema schema) {
        this.kind = Kind.SCHEMA;
        this.schema = schema;
        this.properties = Collections.emptyList ();
    }

    public JsonDependency (Collection<String> values) {
        this.kind = Kind.ARRAY;
        this.schema = new JsonSchemaObject (Collections.emptyMap ());
        this.properties = values;
    }

    public boolean isSchema () {
        return kind == Kind.SCHEMA;
    }

    public JsonSchema getSchema () {
        return schema;
    }

    public Collection<String> getProperties () {
        return properties;
    }
}

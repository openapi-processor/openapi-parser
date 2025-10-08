/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator;

import io.openapiprocessor.jsonschema.schema.JsonInstance;
import io.openapiprocessor.jsonschema.schema.JsonSchema;

import java.util.*;

// todo interface
public class ValidationMessage {
    private final String property;
    private final String text;

    private final JsonSchema schema;
    private final JsonInstance instance;

    public ValidationMessage (JsonSchema schema, JsonInstance instance, String property, String text) {
        this.property = property;
        this.text = text;
        this.schema = schema;
        this.instance = instance;
    }

    public String getSchemaScope () {
        return schema.getContext ().getScope ().toString ();
    }

    public String getSchemaPath () {
        return schema.getLocation ().toString ();
    }

    public String getInstancePath () {
        return instance.getPath ();
    }

    public String getProperty () {
        return property;
    }

    public String getText () {
        return text;
    }

    @Override
    public String toString () {
        return String.format ("%s (instance: %s)  (schema: %s)", text,
            getInstancePath (), getSchemaPath ());
    }
}
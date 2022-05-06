/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.*;

// todo interface
public class ValidationMessage {
    private final String text;

    private final JsonSchema schema;
    private final JsonInstance instance;

    private Collection<ValidationMessage> nestedMessage = Collections.emptyList ();

    public ValidationMessage (JsonSchema schema, JsonInstance instance, String text) {
        this.text = text;
        this.schema = schema;
        this.instance = instance;
    }

    public ValidationMessage (
        JsonSchema schema,
        JsonInstance instance,
        String text,
        Collection<ValidationMessage> nestedMessage
    ) {
        this.text = text;
        this.schema = schema;
        this.instance = instance;
        this.nestedMessage = nestedMessage;
    }

    public String getSchemaScope () {
        return schema.getContext ().getScope ().toString ();
    }

    public String getSchemaPath () {
        return schema.getLocation ().toString ();
    }

    public String getInstanceScope () {
        return instance.getScope ().toString ();
    }

    public String getInstancePath () {
        return instance.getPath ();
    }

    public String getText () {
        return text;
    }

    public boolean hasNestedMessages () {
        return ! nestedMessage.isEmpty ();
    }

    public Collection<ValidationMessage> getNestedMessages () {
        return nestedMessage;
    }

    @Override
    public String toString () {
        return String.format ("%s (instance: %s)  (schema: %s)", text,
            getInstancePath (), getSchemaPath ());
    }
}

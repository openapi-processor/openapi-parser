/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;

import java.util.*;

// todo interface
public class ValidationMessage {
    private final String text;

    private final JsonSchema schema;
    private final JsonInstance instance;

    private Collection<ValidationMessage> nestedMessage = Collections.emptyList ();

    @Deprecated private final String scope;
    @Deprecated private final String path;

    @Deprecated
    public ValidationMessage (String path, String text) {
        this.text = text;
        this.schema = null;
        this.instance = null;
        this.scope = "?";
        this.path = path;
    }

    @Deprecated
    public ValidationMessage (String scope, String path, String text) {
        this.text = text;
        this.schema = null;
        this.instance = null;
        this.scope = scope;
        this.path = path;
    }

    public ValidationMessage (JsonSchema schema, JsonInstance instance, String text) {
        this.text = text;
        this.schema = schema;
        this.instance = instance;
        this.scope = instance.getScope ().toString ();
        this.path = instance.getPath ();
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
        this.scope = instance.getScope ().toString ();
        this.path = instance.getPath ();
        this.nestedMessage = nestedMessage;
    }

    @Deprecated
    public String getScope () {
        return scope;
    }

    @Deprecated
    public String getPath () {
        return path;
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

    public Collection<ValidationMessage> getNestedMessages () {
        return nestedMessage;
    }

    @Override
    public String toString () {
        return String.format ("%s: %s", path, text);
    }
}

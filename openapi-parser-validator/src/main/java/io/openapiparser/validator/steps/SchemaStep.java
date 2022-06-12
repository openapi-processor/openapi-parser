/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.steps;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.ValidationMessage;

import java.util.Collection;
import java.util.Collections;

public class SchemaStep extends CompositeStep {
    private final JsonSchema schema;
    private final JsonInstance instance;

    public SchemaStep (JsonSchema schema, JsonInstance instance) {
        this.schema = schema;
        this.instance = instance;
    }

    @Override
    public Collection<ValidationMessage> getMessages () {
        if (isValid ())
            return Collections.emptyList ();

        return Collections.singletonList (
            new ValidateError (schema, instance, super.getMessages ()));
    }

    @Override
    public String toString () {
        return String.format ("%s (instance: %s), (schema: %s)",
            isValid () ? "valid" : "invalid",
            instance.toString ().isEmpty () ? "/" : instance.toString (),
            schema.toString ().isEmpty () ? "/" : schema.toString ());
    }
}

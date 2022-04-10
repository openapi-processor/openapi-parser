/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.steps;

import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.ValidationMessage;

import java.util.Collection;
import java.util.Collections;

public class SchemaRefStep implements ValidationStep {
    private final JsonSchema schema;

    public SchemaRefStep (JsonSchema schema) {
        this.schema = schema;
    }

    @Override
    public Collection<ValidationStep> getSteps () {
        return Collections.singletonList (this);
    }

    @Override
    public Collection<ValidationMessage> getMessages () {
        return Collections.emptyList ();
    }

    @Override
    public boolean isValid () {
        return true;
    }

    @Override
    public String toString () {
        return String.format ("%s", schema.toString ());
    }
}

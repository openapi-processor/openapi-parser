/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.steps;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.ValidationMessage;

/**
 * simpler?? may replace a number of {@code ValidationStep} implementations.
 */
public class ValidatedStep extends SimpleStep {
    private ValidationMessage message;

    public ValidatedStep (JsonSchema schema, JsonInstance instance, String property) {
        super (schema, instance, property);
    }

    public void setError (ValidationMessage message) {
        setInvalid ();
        this.message = message;
    }

    @Override
    public ValidationMessage getError () {
        return message;
    }

    @Override
    public String toString () {
        return String.format ("%s (%s)", super.toString (), property);
    }
}

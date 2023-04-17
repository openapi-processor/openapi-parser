/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.any;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.schema.Keywords;
import io.openapiparser.validator.ValidationMessage;
import io.openapiparser.validator.steps.CompositeStep;
import io.openapiparser.validator.steps.ValidationStep;

import java.util.Collection;
import java.util.Collections;

public class AnyOfStep extends CompositeStep {
    private final JsonSchema schema;
    private final JsonInstance instance;
    private boolean valid = true;

    public AnyOfStep (JsonSchema schema, JsonInstance instance) {
        this.schema = schema;
        this.instance = instance;
    }

    public void setInvalid () {
        valid = false;
    }

    public int countValid () {
        return (int) steps.stream ()
            .filter (ValidationStep::isValid)
            .count ();
    }

    @Override
    public Collection<ValidationMessage> getMessages () {
        if (isValid ())
            return Collections.emptyList ();

        return Collections.singletonList (
            new AnyOfError (schema, instance, super.getMessages ()));
    }

    @Override
    public boolean isValid () {
        return valid;
    }

    @Override
    public String toString () {
        return String.format ("%s (instance: %s), (schema: %s)",
            isValid () ? "valid" : "invalid",
            instance.getLocation (),
            schema.getLocation ().append (Keywords.ANY_OF));
    }
}

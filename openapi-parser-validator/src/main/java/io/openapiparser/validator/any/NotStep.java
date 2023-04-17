/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.any;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.schema.Keywords;
import io.openapiparser.validator.ValidationMessage;
import io.openapiparser.validator.steps.ValidationStep;

import java.util.Collection;
import java.util.Collections;

public class NotStep implements ValidationStep {
    private final JsonSchema schema;
    private final JsonInstance instance;
    private boolean valid = true;

    private ValidationStep step;

    public NotStep (JsonSchema schema, JsonInstance instance) {
        this.schema = schema;
        this.instance = instance;
    }

    @Override
    public void add (ValidationStep step) {
        this.step = step;
    }

    @Override
    public Collection<ValidationStep> getSteps () {
        return Collections.singletonList (this);
    }

    @Override
    public Collection<ValidationMessage> getMessages () {
        if (isValid ())
            return Collections.emptyList ();

        return Collections.singletonList (new NotError (schema, instance, step.getMessages ()));
    }

    @Override
    public boolean isValid () {
        return ! step.isValid ();
    }

    public void setInvalid () {
        this.valid = false;
    }

    @Override
    public String toString () {
        return String.format ("%s (instance: %s), (schema: %s)",
            isValid () ? "valid" : "invalid",
            instance.toString ().isEmpty () ? "/" : instance.toString (),
            schema.getLocation ().append (Keywords.NOT));
    }
}

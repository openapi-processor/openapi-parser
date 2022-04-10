/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.any;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.ValidationMessage;
import io.openapiparser.validator.steps.ValidationStep;

import java.util.Collection;
import java.util.Collections;

public class NotStep implements ValidationStep {
    private final JsonSchema schema;
    private final JsonInstance instance;
    private boolean valid = true;

    private final ValidationStep step;

    public NotStep (JsonSchema schema, JsonInstance instance, ValidationStep step) {
        this.schema = schema;
        this.instance = instance;
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

    @Override
    public String toString () {
        return isValid () ? "valid" : "invalid";
    }
}

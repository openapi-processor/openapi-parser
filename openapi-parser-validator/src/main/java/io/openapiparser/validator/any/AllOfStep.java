/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.any;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.ValidationMessage;
import io.openapiparser.validator.steps.CompositeStep;

import java.util.Collection;
import java.util.Collections;

public class AllOfStep extends CompositeStep {
    private final JsonSchema schema;
    private final JsonInstance instance;
    private boolean valid = true;

    public AllOfStep (JsonSchema schema, JsonInstance instance) {
        this.schema = schema;
        this.instance = instance;
    }

    public void setInvalid () {
        valid = false;
    }

    @Override
    public Collection<ValidationMessage> getMessages () {
        if (isValid ())
            return Collections.emptyList ();

        return Collections.singletonList (
            new AllOfError (schema, instance, super.getMessages ()));
    }

    @Override
    public boolean isValid () {
        return valid;
    }
}

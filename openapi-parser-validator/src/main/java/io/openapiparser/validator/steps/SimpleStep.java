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

public abstract class SimpleStep implements ValidationStep {
    protected final JsonSchema schema;
    protected final JsonInstance instance;
    protected final String property;
    private boolean valid = true;

    public SimpleStep (JsonSchema schema, JsonInstance instance) {
        this.schema = schema;
        this.instance = instance;
        this.property = "n/a";
    }

    public SimpleStep (JsonSchema schema, JsonInstance instance, String property) {
        this.schema = schema;
        this.instance = instance;
        this.property = property;
    }

    protected abstract ValidationMessage getError ();

    @Override
    public Collection<ValidationStep> getSteps () {
        return Collections.singletonList (this);
    }

    @Override
    public Collection<ValidationMessage> getMessages () {
        if (valid)
            return Collections.emptyList ();

        return Collections.singletonList (getError());
    }

    @Override
    public boolean isValid () {
        return valid;
    }

    public void setInvalid () {
        valid = false;
    }

    public void setValid (boolean valid) {
        this.valid = valid;
    }

    @Override
    public String toString () {
        return isValid () ? "valid" : "invalid";
    }
}

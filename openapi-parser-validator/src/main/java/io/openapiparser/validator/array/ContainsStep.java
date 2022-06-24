/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.array;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.ValidationMessage;
import io.openapiparser.validator.steps.CompositeStep;

import java.util.Collection;
import java.util.Collections;

public class ContainsStep extends CompositeStep {
    private final JsonSchema schema;
    private final JsonInstance instance;

    private boolean valid = true;

    public ContainsStep (JsonSchema schema, JsonInstance instance) {
        this.schema = schema;
        this.instance = instance;
    }

    @Override
    public boolean isValid () {
        return valid;
    }

    public void setInvalid () {
        valid = false;
    }

    @Override
    public Collection<ValidationMessage> getMessages () {
        if (valid)
            return Collections.emptyList ();

        return Collections.singletonList (getError());
    }

    private ValidationMessage getError () {
        return new ContainsError (schema, instance, super.getMessages ());
    }
}

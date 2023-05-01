/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.any;

import io.openapiparser.schema.*;
import io.openapiparser.validator.ValidationMessage;
import io.openapiparser.validator.steps.CompositeStep;
import io.openapiparser.validator.steps.Step;
import io.openapiparser.validator.steps.ValidationStep;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;

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

    public int countValid () {
        return (int) steps.stream ()
            .filter (ValidationStep::isValid)
            .count ();
    }

    @Override
    public @Nullable ValidationMessage getMessage () {
        if (valid)
            return null;

        return new AllOfError (schema, instance);
    }

    @Override
    public boolean isValid () {
        return valid;
    }

    public JsonPointer getInstanceLocation () {
        return instance.getLocation ();
    }

    @Override
    public JsonPointer getKeywordLocation () {
        return schema.getLocation ().append (Keywords.ALL_OF);
    }

    @Override
    public URI getAbsoluteKeywordLocation () {
        return Step.getAbsoluteKeywordLocation (getScope (), getKeywordLocation ());
    }

    @Override
    public String toString () {
        return Step.toString (getKeywordLocation (), getInstanceLocation (), valid);
    }

    private Scope getScope () {
        return schema.getContext ().getScope ();
    }
}

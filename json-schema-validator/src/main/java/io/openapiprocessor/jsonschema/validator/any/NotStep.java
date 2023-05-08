/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.any;

import io.openapiprocessor.jsonschema.schema.*;
import io.openapiprocessor.jsonschema.validator.ValidationMessage;
import io.openapiprocessor.jsonschema.validator.steps.CompositeStep;
import io.openapiprocessor.jsonschema.validator.steps.Step;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;

public class NotStep extends CompositeStep {
    private final JsonSchema schema;
    private final JsonInstance instance;
    private boolean valid = true;

    public NotStep (JsonSchema schema, JsonInstance instance) {
        this.schema = schema;
        this.instance = instance;
    }

    @Override
    public @Nullable ValidationMessage getMessage () {
        if (!isValid ())
            return new NotError (schema, instance);

        return null;
    }

    @Override
    public boolean isValid () {
        return ! super.isValid ();
    }

    public void setInvalid () {
        valid = false;
    }

    @Override
    public JsonPointer getInstanceLocation () {
        return instance.getLocation ();
    }

    @Override
    public JsonPointer getKeywordLocation () {
        return schema.getLocation ().append (Keywords.NOT);
    }

    @Override
    public URI getAbsoluteKeywordLocation () {
        return Step.getAbsoluteKeywordLocation (getScope (), getKeywordLocation ());
    }

    @Override
    public String toString () {
        return Step.toString (getKeywordLocation (), getInstanceLocation (), isValid ());
    }

    private Scope getScope () {
        return schema.getContext ().getScope ();
    }
}

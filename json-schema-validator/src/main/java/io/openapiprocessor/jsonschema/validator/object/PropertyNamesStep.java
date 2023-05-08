/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.object;

import io.openapiprocessor.jsonschema.schema.*;
import io.openapiprocessor.jsonschema.validator.steps.CompositeStep;
import io.openapiprocessor.jsonschema.validator.steps.Step;

import java.net.URI;

public class PropertyNamesStep extends CompositeStep {
    private final JsonSchema schema;
    private final JsonInstance instance;

    public PropertyNamesStep (JsonSchema schema, JsonInstance instance) {
        this.schema = schema;
        this.instance = instance;
    }

    @Override
    public JsonPointer getInstanceLocation () {
        return instance.getLocation ();
    }

    @Override
    public JsonPointer getKeywordLocation () {
        return schema.getLocation ().append (Keywords.PROPERTY_NAMES);
    }

    @Override
    public URI getAbsoluteKeywordLocation () {
        return Step.getAbsoluteKeywordLocation (getScope (), getKeywordLocation ());
    }

    protected Scope getScope () {
        return schema.getContext ().getScope ();
    }

    @Override
    public String toString () {
        return Step.toString (getKeywordLocation (), getInstanceLocation (), isValid ());
    }
}

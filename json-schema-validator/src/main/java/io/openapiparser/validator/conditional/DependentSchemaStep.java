/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.conditional;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonPointer;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.schema.Scope;
import io.openapiparser.validator.Annotation;
import io.openapiparser.validator.ValidationMessage;
import io.openapiparser.validator.steps.CompositeStep;
import io.openapiparser.validator.steps.Step;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;

public class DependentSchemaStep extends CompositeStep {

    private final JsonSchema schema;
    private final JsonInstance instance;

    public DependentSchemaStep (JsonSchema schema, JsonInstance instance) {
        this.schema = schema;
        this.instance = instance;
    }

    @Override
    public @Nullable ValidationMessage getMessage () {
        return null;
    }

    @Override
    public @Nullable Annotation getAnnotation () {
        return null;
    }

    public JsonPointer getInstanceLocation () {
        return instance.getLocation ();
    }

    @Override
    public JsonPointer getKeywordLocation () {
        return schema.getLocation ();
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

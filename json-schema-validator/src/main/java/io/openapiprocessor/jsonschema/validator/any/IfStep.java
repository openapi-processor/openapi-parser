/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.any;

import io.openapiprocessor.jsonschema.schema.*;
import io.openapiprocessor.jsonschema.validator.Annotation;
import io.openapiprocessor.jsonschema.validator.ValidationMessage;
import io.openapiprocessor.jsonschema.validator.steps.Step;
import io.openapiprocessor.jsonschema.validator.steps.ValidationStep;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;


public class IfStep implements ValidationStep {
    private final JsonSchema schema;
    private final JsonInstance instance;

    private final Collection<ValidationStep> steps = new ArrayList<> ();

    public IfStep (JsonSchema schema, JsonInstance instance) {
        this.schema = schema;
        this.instance = instance;
    }

    @Override
    public void add (ValidationStep step) {
        steps.add (step);
    }

    @Override
    public boolean isValidatable () {
        return false;
    }

    @Override
    public boolean isValid () {
        return steps.stream ()
            .filter (ValidationStep::isValidatable)
            .allMatch (ValidationStep::isValid);
    }

    @Override
    public Collection<ValidationStep> getSteps () {
        return steps;
    }

    public @Nullable ValidationMessage getMessage () {
        return null;
    }

    public @Nullable Annotation getAnnotation () {
        return null;
    }

    @Override
    public Collection<Annotation> getAnnotations (String keyword) {
        return steps.stream ()
            .filter (s -> s.isValidatable () && s.isValid ())
            .map (s -> s.getAnnotations (keyword))
            .flatMap (Collection::stream)
            .collect(Collectors.toList ());
    }

    @Override
    public JsonPointer getInstanceLocation () {
        return instance.getLocation ();
    }

    @Override
    public JsonPointer getKeywordLocation () {
        return schema.getLocation ().append (Keywords.IF);
    }

    @Override
    public URI getAbsoluteKeywordLocation () {
        return Step.getAbsoluteKeywordLocation (getScope(), getKeywordLocation ());
    }

    @Override
    public String toString () {
        return Step.toString (getKeywordLocation (), getInstanceLocation (), isValid ());
    }

    private Scope getScope () {
        return schema.getContext ().getScope ();
    }
}

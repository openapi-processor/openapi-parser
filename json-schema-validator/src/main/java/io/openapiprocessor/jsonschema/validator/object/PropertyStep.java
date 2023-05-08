/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.object;

import io.openapiprocessor.jsonschema.schema.JsonInstance;
import io.openapiprocessor.jsonschema.schema.JsonPointer;
import io.openapiprocessor.jsonschema.schema.JsonSchema;
import io.openapiprocessor.jsonschema.schema.Scope;
import io.openapiprocessor.jsonschema.validator.Annotation;
import io.openapiprocessor.jsonschema.validator.ValidationMessage;
import io.openapiprocessor.jsonschema.validator.steps.Step;
import io.openapiprocessor.jsonschema.validator.steps.ValidationStep;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class PropertyStep implements ValidationStep {
    protected final JsonSchema schema;
    protected final JsonInstance instance;
    private final String propertyName;  // not used?????

    protected final Collection<ValidationStep> steps = new ArrayList<> ();

    public PropertyStep (JsonSchema schema, JsonInstance instance, String propertyName) {
        this.schema = schema;
        this.instance = instance;
        this.propertyName = propertyName;
    }

    @Override
    public void add (ValidationStep step) {
        steps.add (step);
    }

    @Override
    public boolean isValid () {
        return steps.stream ()
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
            .filter (ValidationStep::isValid)
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
        return schema.getLocation ();  // append PropertyName???
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

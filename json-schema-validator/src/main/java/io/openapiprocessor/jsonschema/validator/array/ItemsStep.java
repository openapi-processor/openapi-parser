/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.array;

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
import java.util.*;

public class ItemsStep implements ValidationStep {
    private final JsonSchema schema;
    private final JsonInstance instance;
    private final String propertyName;
    private final Collection<ValidationStep> steps = new ArrayList<> ();
    private final Map<String, Annotation> annotations = new HashMap<> ();

    public ItemsStep (JsonSchema schema, JsonInstance instance, String propertyName) {
        this.schema = schema;
        this.instance = instance;
        this.propertyName = propertyName;
    }

    public boolean isNotEmpty () {
        return !isEmpty ();
    }

    public boolean isEmpty () {
        return steps.isEmpty ();
    }

    public void add (ValidationStep step) {
        steps.add (step);
    }

    @Override
    public Collection<ValidationStep> getSteps () {
        return steps;
    }

    @Override
    public @Nullable ValidationMessage getMessage () {
        return null;
    }

    @Override
    public @Nullable Annotation getAnnotation () {
        return annotations.get (propertyName);
    }

    @Override
    public boolean isValid () {
        return steps.stream ()
            .allMatch (ValidationStep::isValid);
    }

    @Override
    public Collection<Annotation> getAnnotations (/*@Nullable*/ String keyword) {
        Annotation annotation = annotations.get (keyword);
        if (annotation == null)
            return Collections.emptyList ();
        else
            return Collections.singletonList (annotation);
    }

    public void addAnnotation (Object value) {
        annotations.put (propertyName, new Annotation (propertyName, value));
    }

    @Override
    public JsonPointer getKeywordLocation () {
        return schema.getLocation ().append (propertyName);
    }

    @Override
    public JsonPointer getInstanceLocation () {
        return instance.getLocation ();
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

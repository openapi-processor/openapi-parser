/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.array;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonPointer;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.schema.Scope;
import io.openapiparser.validator.Annotation;
import io.openapiparser.validator.ValidationMessage;
import io.openapiparser.validator.steps.Step;
import io.openapiparser.validator.steps.ValidationStep;
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

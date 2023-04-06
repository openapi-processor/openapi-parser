/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.object;

import io.openapiparser.validator.Annotation;
import io.openapiparser.validator.ValidationMessage;
import io.openapiparser.validator.steps.ValidationStep;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class PropertiesStep implements ValidationStep {
    private final String propertyName;
    private final Collection<ValidationStep> steps = new ArrayList<> ();
    private final Map<String, Annotation> annotations = new HashMap<> ();


    public PropertiesStep (String propertyName) {
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
    public Collection<ValidationMessage> getMessages () {
        return steps.stream ()
            .map (ValidationStep::getMessages)
            .flatMap (Collection::stream)
            .collect(Collectors.toList ());
    }

    @Override
    public boolean isValid () {
        return steps.stream ()
            .allMatch (ValidationStep::isValid);
    }

    public void addAnnotation (Object value) {
        annotations.put (propertyName, new Annotation (propertyName, value));
    }

    public @Nullable Annotation getAnnotation () {
        return annotations.get (propertyName);
    }

    @Override
    public Collection<Annotation> getAnnotations (@Nullable String keyword) {
        if (keyword == null)
            return Collections.emptyList ();

        Annotation annotation = annotations.get (keyword);
        if (annotation == null)
            return Collections.emptyList ();

        return Collections.singletonList (annotation);
    }

    @Override
    public String toString () {
        return String.format ("%s (property: %s)",
            isValid () ? "valid" : "invalid",
            propertyName);
    }
}

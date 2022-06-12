/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.object;

import io.openapiparser.validator.Annotation;
import io.openapiparser.validator.ValidationMessage;
import io.openapiparser.validator.steps.*;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;

public class PropertyStep implements ValidationStep {
    private final String propertyName;

    private final ValidationStep step;
    private @Nullable Annotation annotation;

    public PropertyStep (String propertyName, ValidationStep step) {
        this.propertyName = propertyName;
        this.step = step;
    }

    @Override
    public boolean isValid () {
        return step.isValid ();
    }

    @Override
    public Collection<ValidationStep> getSteps () {
        return step.getSteps ();
    }

    @Override
    public Collection<ValidationMessage> getMessages () {
        return step.getMessages ();
    }

    public void addAnnotation (Object value) {
        annotation = new Annotation (propertyName, value);
    }

    public @Nullable Annotation getAnnotation () {
        return annotation;
    }

    @Override
    public String toString () {
        return String.format ("%s (property: %s)",
            isValid () ? "valid" : "invalid",
            propertyName);
    }
}

/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.object;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.Annotation;
import io.openapiparser.validator.ValidationMessage;
import io.openapiparser.validator.steps.*;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;

public class PropertyStep implements ValidationStep {
    protected final JsonSchema schema;
    protected final JsonInstance instance;
    private final String propertyName;

    private final ValidationStep step;
    //private @Nullable Annotation annotation;

    public PropertyStep (JsonSchema schema, JsonInstance instance, String propertyName, ValidationStep step) {
        this.schema = schema;
        this.instance = instance;
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

    @Override
    public Collection<Annotation> getAnnotations (String keyword) {
        return step.getAnnotations (keyword);
    }

    @Override
    public String toString () {
        return String.format ("%s (instance: %s), (schema: %s)",
            isValid () ? "valid" : "invalid",
            instance.getLocation (),
            schema.getLocation ());
    }
}

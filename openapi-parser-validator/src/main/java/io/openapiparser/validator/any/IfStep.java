/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.any;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.Annotation;
import io.openapiparser.validator.ValidationMessage;
import io.openapiparser.validator.steps.NullStep;
import io.openapiparser.validator.steps.ValidationStep;

import java.util.*;


public class IfStep implements ValidationStep {
    private final JsonSchema schema;
    private final JsonInstance instance;

    private ValidationStep stepIf = new NullStep ();
    private ValidationStep stepThen = new NullStep ();
    private ValidationStep stepElse = new NullStep ();

    public IfStep (JsonSchema schema, JsonInstance instance) {
        this.schema = schema;
        this.instance = instance;
    }

    public void setIf(ValidationStep stepIf) {
        this.stepIf = stepIf;
    }

    public void setThen(ValidationStep stepThen) {
        this.stepThen = stepThen;
    }

    public void setElse(ValidationStep stepElse) {
        this.stepElse = stepElse;
    }

    @Override
    public boolean isValid () {
        if (stepIf.isValid ()) {
            return stepThen.isValid ();
        } else {
            return stepElse.isValid ();
        }
    }

    @Override
    public Collection<ValidationStep> getSteps () {
        if (stepIf.isValid ()) {
            return Collections.singletonList (stepThen);
        } else {
            return Collections.singletonList (stepElse);
        }
    }

    @Override
    public Collection<ValidationMessage> getMessages () {
        if (isValid ())
            return Collections.emptyList ();

        if (stepIf.isValid ()) {
            return Collections.singletonList (
                new IfError (schema, instance, "then", stepThen.getMessages ()));
        } else {
            return Collections.singletonList (
                new IfError (schema, instance, "else", stepElse.getMessages ()));
        }
    }

    @Override
    public Collection<Annotation> getAnnotations (String keyword) {
        if (stepIf.isValid ()) {
            Collection<Annotation> annotations = new ArrayList<> ();
            annotations.addAll (stepIf.getAnnotations (keyword));
            annotations.addAll (stepThen.getAnnotations (keyword));
            return annotations;
        } else {
            return stepElse.getAnnotations (keyword);
        }
    }
}

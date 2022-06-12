/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.steps;

import io.openapiparser.validator.Annotation;
import io.openapiparser.validator.ValidationMessage;

import java.util.*;
import java.util.stream.Collectors;

public class CompositeStep implements ValidationStep {
    protected final Collection<ValidationStep> steps;

    public CompositeStep () {
        this.steps = new ArrayList<> ();
    }

    public CompositeStep (Collection<ValidationStep> steps) {
        this.steps = steps;
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

    public Collection<ValidationStep> getSteps () {
        return Collections.singletonList (this);
    }

    @Override
    public Collection<ValidationMessage> getMessages () {
        return steps.stream ()
            .map (ValidationStep::getMessages)
            .flatMap (Collection::stream)
            .collect(Collectors.toList ());
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
    public boolean isValid () {
        return steps.stream ()
            .allMatch (ValidationStep::isValid);
    }

    @Override
    public String toString () {
        return isValid () ? "valid" : "invalid";
    }
}

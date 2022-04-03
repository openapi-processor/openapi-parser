/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.steps;

import io.openapiparser.validator.ValidationMessage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class CompositeStep implements ValidationStep {
    private final Collection<ValidationStep> steps;

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
        steps.addAll (step.getSteps ());
    }

    public Collection<ValidationStep> getSteps () {
        return steps;
//        return steps.stream ()
//            .map (ValidationStep::getSteps)
//            .flatMap (Collection::stream)
//            .collect (Collectors.toList ());
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
            .allMatch (s -> isValid ());
    }
}

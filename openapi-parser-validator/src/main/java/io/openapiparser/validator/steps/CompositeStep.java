/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.steps;

import io.openapiparser.validator.*;

import java.util.*;
import java.util.stream.Collectors;

public class CompositeStep implements ValidationStep, Annotations {
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
        if (isNullStep(step))
            return;

        if (isFlatStep(step)) {
            steps.addAll (step.getSteps ());
        } else {
            steps.add (step);
        }
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

    public AnnotationsComposite mergeAnnotations (Annotations annotations) {
        AnnotationsComposite merge = new AnnotationsComposite ();
        merge.add (annotations);
        merge.add (this);
        return merge;
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

    private static boolean isNullStep(ValidationStep step) {
        return step instanceof NullStep;
    }

    private static boolean isFlatStep(ValidationStep step) {
        return step instanceof FlatStep;
    }
}

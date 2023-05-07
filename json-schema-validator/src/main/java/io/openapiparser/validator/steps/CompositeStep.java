/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.steps;

import io.openapiparser.validator.*;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public abstract class CompositeStep implements ValidationStep, Annotations {
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

    public @Nullable ValidationMessage getMessage () {
        return null;
    }

    public @Nullable Annotation getAnnotation () {
        return null;
    }

    @Override
    public Collection<ValidationStep> getSteps () {
        return steps;
    }

    @Override
    public Collection<Annotation> getAnnotations (String keyword) {
        return steps.stream ()
            .filter (ValidationStep::isValid)
            .map (s -> s.getAnnotations (keyword))
            .flatMap (Collection::stream)
            .collect(Collectors.toList ());
    }

    // todo not needed ??
    public AnnotationsComposite mergeAnnotations (Annotations annotations) {
        AnnotationsComposite merge = new AnnotationsComposite ();
        merge.add (annotations);
        merge.add (this);
        return merge;
    }

    @Override
    public boolean isValid () {
        return steps.stream ()
            .filter (ValidationStep::isValidatable)
            .allMatch (ValidationStep::isValid);
    }

    @Override
    public String toString () {
        return isValid () ? "(composite) valid" : "invalid";
    }
}

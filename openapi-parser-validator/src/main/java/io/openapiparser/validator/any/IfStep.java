/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.any;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.schema.Keywords;
import io.openapiparser.validator.Annotation;
import io.openapiparser.validator.ValidationMessage;
import io.openapiparser.validator.steps.ValidationStep;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;


public class IfStep implements ValidationStep {
    private final JsonSchema schema;
    private final JsonInstance instance;

    private final Collection<ValidationStep> steps = new ArrayList<> ();

    public IfStep (JsonSchema schema, JsonInstance instance) {
        this.schema = schema;
        this.instance = instance;
    }

    @Override
    public void add (ValidationStep step) {
        steps.add (step);
    }

    @Override
    public boolean isValidatable () {
        return false;
    }

    @Override
    public boolean isValid () {
        return steps.stream ()
            .filter (ValidationStep::isValidatable)
            .allMatch (ValidationStep::isValid);
    }

    @Override
    public Collection<ValidationStep> getSteps () {
        return steps;
    }

    @Override
    public Collection<ValidationMessage> getMessages () {
            return Collections.emptyList ();
    }

    @Override
    public Collection<Annotation> getAnnotations (String keyword) {
        return steps.stream ()
            .filter (s -> s.isValidatable () && s.isValid ())
            .map (s -> s.getAnnotations (keyword))
            .flatMap (Collection::stream)
            .collect(Collectors.toList ());
    }

    @Override
    public String toString () {
        return String.format ("%s (instance: %s), (schema: %s)",
            isValid () ? "valid" : "invalid",
            instance.toString ().isEmpty () ? "/" : instance.toString (),
            schema.getLocation ().append (Keywords.IF));
    }
}

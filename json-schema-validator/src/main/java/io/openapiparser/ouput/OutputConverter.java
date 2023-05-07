/*
 * Copyright 2023 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.ouput;

import io.openapiparser.schema.Output;
import io.openapiparser.schema.UriSupport;
import io.openapiparser.validator.Annotation;
import io.openapiparser.validator.ValidationMessage;
import io.openapiparser.validator.steps.DynamicRefStep;
import io.openapiparser.validator.steps.RefStep;
import io.openapiparser.validator.steps.ValidationStep;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;

public class OutputConverter {
    static class RefLocation {
        private final @Nullable String source;
        private final String target;

        public RefLocation (@Nullable String source, String target) {
            this.source = source;
            this.target = target;
        }

        public @Nullable String getSource () {
            return source;
        }

        public String getTarget () {
            return target;
        }
    }

    private final Output output;

    public OutputConverter (Output output) {
        this.output = output;
    }

    public OutputUnit convert (ValidationStep step) {
        if (Output.FLAG.equals (output)) {
            return flag (step);
        } else if (Output.VERBOSE.equals (output)) {
            return verbose (step, null);
        }

        throw new RuntimeException ();
    }

    private OutputUnit flag (ValidationStep step) {
        return new OutputUnitFlag (step.isValid ());
    }

    // pass parent step
    private OutputUnit verbose (ValidationStep step, @Nullable RefLocation parentRefLocation) {
        Collection<OutputUnit> annotations = new ArrayList<> ();
        Collection<OutputUnit> errors = new ArrayList<> ();

        RefLocation refLocation = parentRefLocation;
        if (step instanceof RefStep) {
            RefStep refStep = (RefStep) step;
            URI ref = refStep.getRef ();
            String fragment = ref.getFragment (); // != null ? ref.getFragment (): "";
             refLocation = new RefLocation (fragment, getKeywordLocation (step, refLocation));

        } else if (step instanceof DynamicRefStep) {
            DynamicRefStep refStep = (DynamicRefStep) step;

            URI ref = refStep.getRef ();
            String fragment = ref.getFragment (); // != null ? ref.getFragment (): "";
             refLocation = new RefLocation (fragment, getKeywordLocation (step, refLocation));
        }

        for (ValidationStep s : step.getSteps ()) {
            if (s.isValid ()) {
                annotations.add (verbose (s, refLocation));
            } else {
                errors.add (verbose (s, refLocation));
            }
        }

        OutputUnitVerbose verbose = new OutputUnitVerbose ();
        verbose.setValid (step.isValid ());
        verbose.setKeywordLocation (getKeywordLocation (step, refLocation));
        verbose.setInstanceLocation (step.getInstanceLocation ().toString ());
        verbose.setAbsoluteKeywordLocation (getAbsoluteKeywordLocation (step));
        verbose.setError (getError (step));
        verbose.setErrors (!errors.isEmpty () ? errors : null);
        verbose.setAnnotation (getAnnotation (step));
        verbose.setAnnotations (!annotations.isEmpty () ? annotations : null);
        return verbose;
    }

    private @Nullable Object getAnnotation (ValidationStep step) {
        Annotation annotation = step.getAnnotation ();
        if (annotation == null)
            return null;

        return annotation.getValue ();
    }

    private String getKeywordLocation (ValidationStep step, @Nullable RefLocation refLocation) {
        String location = step.getKeywordLocation ().toString ();
        if (refLocation == null) {
            return location;
        }

        String source = refLocation.getSource ();
        if (source == null) {
            return location;
        }

        if (source.equals (location) || source.isEmpty ()) {
            return location;
        }

        if (location.startsWith (source)) {
            location = refLocation.getTarget () + location.substring (source.length ());
        }

        return location;
    }

    private String getAbsoluteKeywordLocation (ValidationStep step) {
        return UriSupport.decode (step.getAbsoluteKeywordLocation ().toString ());
    }

    private @Nullable String getError (ValidationStep step) {
        ValidationMessage message = step.getMessage ();
        if (step.isValid () || message == null)
            return null;

        return message.getText ();
    }
}

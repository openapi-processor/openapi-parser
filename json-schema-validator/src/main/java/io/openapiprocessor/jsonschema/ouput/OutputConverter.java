/*
 * Copyright 2023 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.ouput;

import io.openapiprocessor.jsonschema.schema.Output;
import io.openapiprocessor.jsonschema.schema.UriSupport;
import io.openapiprocessor.jsonschema.validator.Annotation;
import io.openapiprocessor.jsonschema.validator.ValidationMessage;
import io.openapiprocessor.jsonschema.validator.steps.DynamicRefStep;
import io.openapiprocessor.jsonschema.validator.steps.RefStep;
import io.openapiprocessor.jsonschema.validator.steps.ValidationStep;
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

        } else if (Output.BASIC.equals (output)) {
            return basic (step);

        } else if (Output.VERBOSE.equals (output)) {
            return verbose (step, null);
        }

        throw new RuntimeException ();
    }

    private OutputUnit flag (ValidationStep step) {
        return new OutputUnitFlag (step.isValid ());
    }

    private OutputUnit basic (ValidationStep step) {
        OutputUnit verbose = verbose (step, null);

        OutputUnitNode basic = new OutputUnitNode ();
        basic.setValid (verbose.isValid ());

        if (verbose.isValid ()) {
            Collection<OutputUnit> annotations = new ArrayList<> ();
            flattenBasicAnnotations (verbose, annotations);
            basic.setAnnotations (annotations);
        } else {
            Collection<OutputUnit> errors = new ArrayList<> ();
            flattenBasicErrors (verbose, errors);
            basic.setErrors (errors);
        }

        return basic;
    }

    private void flattenBasicAnnotations (OutputUnit verbose, Collection<OutputUnit> result) {
        Object currentAnnotation = verbose.getAnnotation ();
        if (hasAnnotationValue (currentAnnotation)) {
            OutputUnitNode node = new OutputUnitNode ();
            node.setValid (verbose.isValid ());
            node.setKeywordLocation (verbose.getKeywordLocation ());
            node.setInstanceLocation (verbose.getInstanceLocation ());
            node.setAbsoluteKeywordLocation (verbose.getAbsoluteKeywordLocation ());
            node.setAnnotation (verbose.getAnnotation ());
            result.add (node);
        }

        Collection<OutputUnit> annotations = verbose.getAnnotations ();
        if (annotations == null)
            return;

        for (OutputUnit annotation : annotations) {
            flattenBasicAnnotations (annotation, result);
        }
    }

    private void flattenBasicErrors (OutputUnit verbose, Collection<OutputUnit> result) {
        String currentError = verbose.getError ();
        if (currentError != null) {
            OutputUnitNode node = new OutputUnitNode ();
            node.setValid (verbose.isValid ());
            node.setKeywordLocation (verbose.getKeywordLocation ());
            node.setInstanceLocation (verbose.getInstanceLocation ());
            node.setAbsoluteKeywordLocation (verbose.getAbsoluteKeywordLocation ());
            node.setError (verbose.getError ());
            result.add (node);
        }

        Collection<OutputUnit> errors = verbose.getErrors ();
        if (errors == null)
            return;

        for (OutputUnit error : errors) {
            flattenBasicErrors (error, result);
        }
    }

    private boolean hasAnnotationValue (Object annotation) {
        if (annotation == null)
            return false;

        if (annotation instanceof Collection && ((Collection<?>) annotation).isEmpty ())
            return false;

        return true;
    }

    private OutputUnit verbose (ValidationStep step, @Nullable RefLocation parentRefLocation) {
        Collection<OutputUnit> annotations = new ArrayList<> ();
        Collection<OutputUnit> errors = new ArrayList<> ();

        RefLocation refLocation = parentRefLocation;
        if (step instanceof RefStep) {
            RefStep refStep = (RefStep) step;
            URI ref = refStep.getRef ();
            String fragment = ref.getFragment ();
            refLocation = new RefLocation (fragment, getKeywordLocation (step, refLocation));

        } else if (step instanceof DynamicRefStep) {
            DynamicRefStep refStep = (DynamicRefStep) step;

            URI ref = refStep.getRef ();
            String fragment = ref.getFragment ();
            refLocation = new RefLocation (fragment, getKeywordLocation (step, refLocation));
        }

        for (ValidationStep s : step.getSteps ()) {
            if (s.isValid ()) {
                annotations.add (verbose (s, refLocation));
            } else {
                errors.add (verbose (s, refLocation));
            }
        }

        OutputUnitNode verbose = new OutputUnitNode ();
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

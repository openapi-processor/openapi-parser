/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.steps;

import io.openapiprocessor.jsonschema.schema.JsonPointer;
import io.openapiprocessor.jsonschema.validator.Annotation;
import io.openapiprocessor.jsonschema.validator.ValidationMessage;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;
import java.util.Collection;

public interface ValidationStep {
    boolean isValid ();

    Collection<ValidationStep> getSteps ();

    void add (ValidationStep step);

    @Nullable ValidationMessage getMessage ();

    @Nullable Annotation getAnnotation ();

    Collection<Annotation> getAnnotations (String keyword);

    JsonPointer getKeywordLocation ();

    URI getAbsoluteKeywordLocation ();

    JsonPointer getInstanceLocation ();

    /**
     * If true this step is used to calculate the final validation result. 'if' is the only keyword that is not
     * validatable.
     */
    default boolean isValidatable () {
        return true;
    }
}

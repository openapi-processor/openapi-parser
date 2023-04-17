/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.steps;

import io.openapiparser.schema.JsonPointer;
import io.openapiparser.schema.UriSupport;
import io.openapiparser.validator.Annotation;
import io.openapiparser.validator.ValidationMessage;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;

public interface ValidationStep {
    boolean isValid ();
    Collection<ValidationStep> getSteps ();
    Collection<ValidationMessage> getMessages ();

    default void add (ValidationStep step) {
    }

    // todo remove default implementation
    default Collection<Annotation> getAnnotations (String keyword) {
        return Collections.emptyList ();
    }

    default JsonPointer getKeywordLocation() {
        return JsonPointer.empty ();
    }

    default URI getAbsoluteKeywordLocation() {
        return UriSupport.emptyUri ();
    }

    default JsonPointer getInstanceLocation() {
        return JsonPointer.empty ();
    }

    /**
     * If true this step is used to calculate the final validation result. 'if' is the only keyword that is not
     * validatable.
     */
    default boolean isValidatable () {
        return true;
    }
}

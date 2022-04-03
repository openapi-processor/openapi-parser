/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.steps;

import io.openapiparser.validator.ValidationMessage;

import java.util.Collection;

public interface ValidationStep {
    boolean isValid ();
    Collection<ValidationStep> getSteps ();
    Collection<ValidationMessage> getMessages ();
}

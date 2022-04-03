/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.any;

import io.openapiparser.validator.ValidationMessage;
import io.openapiparser.validator.steps.ValidationStep;

import java.util.Collection;
import java.util.Collections;

public class NotStep implements ValidationStep {
    private final ValidationStep step;

    public NotStep (ValidationStep step) {
        this.step = step;
    }

    @Override
    public Collection<ValidationStep> getSteps () {
        return Collections.singletonList (this);
    }

    @Override
    public Collection<ValidationMessage> getMessages () {
        return Collections.emptyList ();
    }

    @Override
    public boolean isValid () {
        return ! step.isValid ();
    }
}

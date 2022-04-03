/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.steps;

import io.openapiparser.validator.ValidationMessage;

import java.util.Collection;
import java.util.Collections;

public class AnyOfStep extends CompositeStep {
    private ValidationMessage message;

    public void set (ValidationMessage message) {
        this.message = message;
    }

    @Override
    public Collection<ValidationMessage> getMessages () {
        if (message == null)
            return Collections.emptyList ();

        return Collections.singletonList (message);
    }

    @Override
    public boolean isValid () {
//        return steps.stream ()
//            .anyMatch (ValidationStep::isValid);

        for (ValidationStep step : steps) {
            boolean valid = step.isValid ();
            if (valid) {
                return true;
            }
        }

        return false;
    }
}

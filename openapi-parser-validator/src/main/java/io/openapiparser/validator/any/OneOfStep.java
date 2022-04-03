/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.any;

import io.openapiparser.validator.ValidationMessage;
import io.openapiparser.validator.steps.CompositeStep;
import io.openapiparser.validator.steps.ValidationStep;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;
import java.util.Collections;

public class OneOfStep extends CompositeStep {
    private @Nullable ValidationMessage message;

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
        int validCount = 0;

        for (ValidationStep step : steps) {
            boolean valid = step.isValid ();
            if (valid) {
                validCount++;
            }
        }

        return validCount == 1;
    }
}

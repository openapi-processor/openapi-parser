/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.steps;

import io.openapiparser.validator.ValidationMessage;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;
import java.util.Collections;

public class SimpleStep implements ValidationStep {
    private final @Nullable ValidationMessage message;

    public SimpleStep () {
        message = null;
    }

    public SimpleStep (@NonNull ValidationMessage message) {
        this.message = message;
    }

    @Override
    public Collection<ValidationStep> getSteps () {
        return Collections.singletonList (this);
    }

    @Override
    public Collection<ValidationMessage> getMessages () {
        if (message == null)
            return Collections.emptyList ();

        return Collections.singletonList (message);
    }

    @Override
    public boolean isValid () {
        return message == null;
    }

    @Override
    public String toString () {
        return isValid () ? "valid" : "invalid";
    }
}

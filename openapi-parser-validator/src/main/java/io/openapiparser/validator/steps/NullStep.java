/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.steps;

import io.openapiparser.validator.ValidationMessage;

import java.util.Collection;
import java.util.Collections;

public class NullStep implements ValidationStep {
    private final String keyword;

    @Deprecated
    public NullStep () {
        this.keyword = "unknown";
    }

    public NullStep (String keyword) {
        this.keyword = keyword;
    }

    @Override
    public Collection<ValidationStep> getSteps () {
        return Collections.emptyList ();
    }

    @Override
    public Collection<ValidationMessage> getMessages () {
        return Collections.emptyList ();
    }

    @Override
    public boolean isValid () {
        return true;
    }

    @Override
    public String toString () {
        return String.format ("no value (%s)", keyword);
    }
}

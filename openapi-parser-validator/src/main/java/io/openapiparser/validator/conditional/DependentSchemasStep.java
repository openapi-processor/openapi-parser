/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.conditional;

import io.openapiparser.validator.steps.CompositeStep;

public class DependentSchemasStep extends CompositeStep {
    private final String propertyName;

    public DependentSchemasStep (String propertyName) {
        this.propertyName = propertyName;
    }

    @Override
    public String toString () {
        return String.format ("%s (property: %s)",
            isValid () ? "valid" : "invalid",
            propertyName);
    }
}

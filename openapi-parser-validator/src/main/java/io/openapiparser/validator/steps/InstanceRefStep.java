/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.steps;

import io.openapiparser.schema.JsonInstance;

public class InstanceRefStep extends CompositeStep {
    private final JsonInstance instance;

    public InstanceRefStep (JsonInstance instance) {
        this.instance = instance;
    }

    @Override
    public String toString () {
        return String.format ("%s", instance.toString ());
    }
}

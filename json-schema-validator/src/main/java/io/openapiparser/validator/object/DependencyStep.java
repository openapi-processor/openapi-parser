/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.object;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.ValidationMessage;
import io.openapiparser.validator.steps.SimpleStep;

public class DependencyStep extends SimpleStep {
    private final String propertyName;

    public DependencyStep (JsonSchema schema, JsonInstance instance, String propertyName) {
        super(schema, instance, propertyName);
        this.propertyName = propertyName;
    }

    @Override
    protected ValidationMessage getError () {
        return new DependenciesError (schema, instance, propertyName);
    }
}

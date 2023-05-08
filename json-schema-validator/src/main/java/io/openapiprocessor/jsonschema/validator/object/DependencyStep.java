/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.object;

import io.openapiprocessor.jsonschema.schema.JsonInstance;
import io.openapiprocessor.jsonschema.schema.JsonSchema;
import io.openapiprocessor.jsonschema.validator.ValidationMessage;
import io.openapiprocessor.jsonschema.validator.steps.SimpleStep;

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

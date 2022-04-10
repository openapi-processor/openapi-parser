/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.object;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.ValidationMessage;
import io.openapiparser.validator.steps.SimpleStep;

public class RequireStep extends SimpleStep {
    private final String propertyName;

    public RequireStep (JsonSchema schema, JsonInstance instance, String propertyName) {
        super(schema, instance);
        this.propertyName = propertyName;
    }

    @Override
    protected ValidationMessage getError () {
        return new RequiredError (schema, instance, propertyName);
    }
}

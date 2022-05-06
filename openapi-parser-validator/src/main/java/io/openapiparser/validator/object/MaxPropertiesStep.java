/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.object;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.ValidationMessage;
import io.openapiparser.validator.steps.SimpleStep;

import static io.openapiparser.support.Nullness.nonNull;

public class MaxPropertiesStep extends SimpleStep {

    public MaxPropertiesStep (JsonSchema schema, JsonInstance instance) {
        super(schema, instance);
    }

    @Override
    protected ValidationMessage getError () {
        return new MaxPropertiesError (schema, instance, nonNull(schema.getMaxProperties ()));
    }
}

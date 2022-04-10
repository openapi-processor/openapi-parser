/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.number;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.ValidationMessage;
import io.openapiparser.validator.steps.SimpleStep;

public class MinimumStep extends SimpleStep {

    public MinimumStep (JsonSchema schema, JsonInstance instance) {
        super(schema, instance);
    }

    @Override
    protected ValidationMessage getError () {
        return new MinimumError (schema, instance);
    }
}

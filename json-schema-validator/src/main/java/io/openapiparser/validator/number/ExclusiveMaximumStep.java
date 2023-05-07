/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.number;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.schema.Keywords;
import io.openapiparser.validator.ValidationMessage;
import io.openapiparser.validator.steps.SimpleStep;

public class ExclusiveMaximumStep extends SimpleStep {

    public ExclusiveMaximumStep (JsonSchema schema, JsonInstance instance) {
        super(schema, instance, Keywords.EXCLUSIVE_MAXIMUM);
    }

    @Override
    protected ValidationMessage getError () {
        return new ExclusiveMaximumError (schema, instance);
    }
}

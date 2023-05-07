/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.string;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.schema.Keywords;
import io.openapiparser.validator.ValidationMessage;
import io.openapiparser.validator.steps.SimpleStep;

public class MinLengthStep extends SimpleStep {

    public MinLengthStep (JsonSchema schema, JsonInstance instance) {
        super(schema, instance, Keywords.MIN_LENGTH);
    }

    @Override
    protected ValidationMessage getError () {
        return new MinLengthError (schema, instance);
    }
}

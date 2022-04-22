/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.number.draft4;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.ValidationMessage;
import io.openapiparser.validator.number.draft4.Maximum4Error;
import io.openapiparser.validator.steps.SimpleStep;

public class Maximum4Step extends SimpleStep {

    public Maximum4Step (JsonSchema schema, JsonInstance instance) {
        super(schema, instance);
    }

    @Override
    protected ValidationMessage getError () {
        return new Maximum4Error (schema, instance);
    }
}

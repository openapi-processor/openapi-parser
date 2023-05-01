/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.number.draft4;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.schema.Keywords;
import io.openapiparser.validator.ValidationMessage;
import io.openapiparser.validator.steps.SimpleStep;

public class Minimum4Step extends SimpleStep {

    public Minimum4Step (JsonSchema schema, JsonInstance instance) {
        super(schema, instance, Keywords.MINIMUM);
    }

    @Override
    protected ValidationMessage getError () {
        return new Minimum4Error (schema, instance);
    }
}

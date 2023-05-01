/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.array;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.schema.Keywords;
import io.openapiparser.validator.ValidationMessage;
import io.openapiparser.validator.steps.SimpleStep;

import static io.openapiparser.support.Nullness.nonNull;

public class MaxItemsStep extends SimpleStep {

    public MaxItemsStep (JsonSchema schema, JsonInstance instance) {
        super(schema, instance, Keywords.MAX_ITEMS);
    }

    @Override
    protected ValidationMessage getError () {
        return new MaxItemsError (schema, instance, nonNull(schema.getMaxItems ()));
    }
}

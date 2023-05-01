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

@Deprecated
public class ItemsSizeStep extends SimpleStep {

    public ItemsSizeStep (JsonSchema schema, JsonInstance instance) {
        super(schema, instance, Keywords.ITEMS);
    }

    @Override
    protected ValidationMessage getError () {
        return new ItemsSizeError (schema, instance);
    }
}

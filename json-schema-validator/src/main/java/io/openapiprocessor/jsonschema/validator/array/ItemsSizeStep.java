/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.array;

import io.openapiprocessor.jsonschema.schema.JsonInstance;
import io.openapiprocessor.jsonschema.schema.JsonSchema;
import io.openapiprocessor.jsonschema.schema.Keywords;
import io.openapiprocessor.jsonschema.validator.ValidationMessage;
import io.openapiprocessor.jsonschema.validator.steps.SimpleStep;

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

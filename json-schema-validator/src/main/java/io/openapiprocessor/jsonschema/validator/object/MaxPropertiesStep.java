/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.object;

import io.openapiprocessor.jsonschema.schema.JsonInstance;
import io.openapiprocessor.jsonschema.schema.JsonSchema;
import io.openapiprocessor.jsonschema.schema.Keywords;
import io.openapiprocessor.jsonschema.validator.ValidationMessage;
import io.openapiprocessor.jsonschema.validator.steps.SimpleStep;

import static io.openapiprocessor.jsonschema.support.Nullness.nonNull;

public class MaxPropertiesStep extends SimpleStep {

    public MaxPropertiesStep (JsonSchema schema, JsonInstance instance) {
        super(schema, instance, Keywords.MAX_PROPERTIES);
    }

    @Override
    protected ValidationMessage getError () {
        return new MaxPropertiesError (schema, instance, nonNull(schema.getMaxProperties ()));
    }
}

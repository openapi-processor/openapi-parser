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

public class MinPropertiesStep extends SimpleStep {

    public MinPropertiesStep (JsonSchema schema, JsonInstance instance) {
        super(schema, instance, Keywords.MIN_PROPERTIES);
    }

    @Override
    protected ValidationMessage getError () {
        return new MinPropertiesError (schema, instance, nonNull(schema.getMinProperties ()));
    }
}

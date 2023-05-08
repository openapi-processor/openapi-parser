/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.bool;

import io.openapiprocessor.jsonschema.schema.JsonInstance;
import io.openapiprocessor.jsonschema.schema.JsonSchema;
import io.openapiprocessor.jsonschema.validator.ValidationMessage;
import io.openapiprocessor.jsonschema.validator.steps.SimpleStep;

public class BooleanStep extends SimpleStep {

    public BooleanStep (JsonSchema schema, JsonInstance instance) {
        super(schema, instance, schema.isTrue () ? "true" : "false");
    }

    @Override
    protected ValidationMessage getError () {
        return new BooleanError (schema, instance);
    }
}

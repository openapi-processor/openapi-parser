/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.any;

import io.openapiprocessor.jsonschema.schema.JsonInstance;
import io.openapiprocessor.jsonschema.schema.JsonSchema;
import io.openapiprocessor.jsonschema.schema.Keywords;
import io.openapiprocessor.jsonschema.validator.ValidationMessage;
import io.openapiprocessor.jsonschema.validator.steps.SimpleStep;

public class ConstStep extends SimpleStep {

    public ConstStep (JsonSchema schema, JsonInstance instance) {
        super(schema, instance, Keywords.CONST);
    }

    @Override
    protected ValidationMessage getError () {
        return new ConstError (schema, instance);
    }
}

/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.number;

import io.openapiprocessor.jsonschema.schema.JsonInstance;
import io.openapiprocessor.jsonschema.schema.JsonSchema;
import io.openapiprocessor.jsonschema.schema.Keywords;
import io.openapiprocessor.jsonschema.validator.ValidationMessage;
import io.openapiprocessor.jsonschema.validator.steps.SimpleStep;

public class ExclusiveMaximumStep extends SimpleStep {

    public ExclusiveMaximumStep (JsonSchema schema, JsonInstance instance) {
        super(schema, instance, Keywords.EXCLUSIVE_MAXIMUM);
    }

    @Override
    protected ValidationMessage getError () {
        return new ExclusiveMaximumError (schema, instance);
    }
}

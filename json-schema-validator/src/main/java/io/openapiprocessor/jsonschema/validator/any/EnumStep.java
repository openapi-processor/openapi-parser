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

public class EnumStep extends SimpleStep {

    public EnumStep (JsonSchema schema, JsonInstance instance) {
        super(schema, instance, Keywords.ENUM);
    }

    @Override
    protected ValidationMessage getError () {
        return new EnumError (schema, instance);
    }
}

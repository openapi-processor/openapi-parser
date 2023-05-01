/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.any;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.schema.Keywords;
import io.openapiparser.validator.ValidationMessage;
import io.openapiparser.validator.steps.SimpleStep;

public class EnumStep extends SimpleStep {

    public EnumStep (JsonSchema schema, JsonInstance instance) {
        super(schema, instance, Keywords.ENUM);
    }

    @Override
    protected ValidationMessage getError () {
        return new EnumError (schema, instance);
    }
}

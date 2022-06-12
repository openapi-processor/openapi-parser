/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.object;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.ValidationMessage;
import io.openapiparser.validator.steps.SimpleStep;

public class PropertyInvalidStep extends SimpleStep {
    public PropertyInvalidStep (JsonSchema schema, JsonInstance instance) {
        super (schema, instance);
    }

    @Override
    protected ValidationMessage getError () {
        // todo InvalidPropertyError
        return null;
    }
}

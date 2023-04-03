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
    private final String propName;

    public PropertyInvalidStep (JsonSchema schema, JsonInstance instance, String propName) {
        super (schema, instance);
        this.propName = propName;
    }

    @Override
    protected ValidationMessage getError () {
        return new PropertyInvalidError (schema, instance, propName);
    }
}

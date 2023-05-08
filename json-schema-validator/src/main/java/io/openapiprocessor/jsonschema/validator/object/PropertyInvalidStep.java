/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.object;

import io.openapiprocessor.jsonschema.schema.JsonInstance;
import io.openapiprocessor.jsonschema.schema.JsonSchema;
import io.openapiprocessor.jsonschema.validator.ValidationMessage;
import io.openapiprocessor.jsonschema.validator.steps.SimpleStep;

public class PropertyInvalidStep extends SimpleStep {

    public PropertyInvalidStep (JsonSchema schema, JsonInstance instance, String propName) {
        super (schema, instance, propName);
    }

    @Override
    protected ValidationMessage getError () {
        return new PropertyInvalidError (schema, instance, property);
    }
}

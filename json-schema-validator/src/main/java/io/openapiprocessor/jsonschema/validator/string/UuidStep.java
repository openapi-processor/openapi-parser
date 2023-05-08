/*
 * Copyright 2023 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.string;

import io.openapiprocessor.jsonschema.schema.JsonInstance;
import io.openapiprocessor.jsonschema.schema.JsonSchema;
import io.openapiprocessor.jsonschema.schema.Keywords;
import io.openapiprocessor.jsonschema.validator.ValidationMessage;
import io.openapiprocessor.jsonschema.validator.steps.SimpleStep;

public class UuidStep extends SimpleStep {

    public UuidStep (JsonSchema schema, JsonInstance instance) {
        super (schema, instance, Keywords.FORMAT);
    }

    @Override
    protected ValidationMessage getError () {
        return new UuidError (schema, instance);
    }
}

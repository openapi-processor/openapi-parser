/*
 * Copyright 2023 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.array;

import io.openapiprocessor.jsonschema.schema.JsonInstance;
import io.openapiprocessor.jsonschema.schema.JsonSchema;
import io.openapiprocessor.jsonschema.schema.Keywords;
import io.openapiprocessor.jsonschema.validator.ValidationMessage;
import io.openapiprocessor.jsonschema.validator.steps.SimpleStep;

import static io.openapiprocessor.jsonschema.support.Nullness.nonNull;

public class MinContainsStep extends SimpleStep {

    public MinContainsStep (JsonSchema schema, JsonInstance instance) {
        super(schema, instance, Keywords.MIN_CONTAINS);
    }

    @Override
    protected ValidationMessage getError () {
        return new MinContainsError (schema, instance, nonNull (schema.getMinContains ()));
    }
}

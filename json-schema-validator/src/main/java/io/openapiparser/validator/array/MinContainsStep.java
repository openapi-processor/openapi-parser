/*
 * Copyright 2023 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.array;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.schema.Keywords;
import io.openapiparser.validator.ValidationMessage;
import io.openapiparser.validator.steps.SimpleStep;

import static io.openapiparser.support.Nullness.nonNull;

public class MinContainsStep extends SimpleStep {

    public MinContainsStep (JsonSchema schema, JsonInstance instance) {
        super(schema, instance, Keywords.MIN_CONTAINS);
    }

    @Override
    protected ValidationMessage getError () {
        return new MinContainsError (schema, instance, nonNull (schema.getMinContains ()));
    }
}

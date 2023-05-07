/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.object;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.schema.Keywords;
import io.openapiparser.validator.ValidationMessage;
import io.openapiparser.validator.steps.SimpleStep;

import static io.openapiparser.support.Nullness.nonNull;

public class MinPropertiesStep extends SimpleStep {

    public MinPropertiesStep (JsonSchema schema, JsonInstance instance) {
        super(schema, instance, Keywords.MIN_PROPERTIES);
    }

    @Override
    protected ValidationMessage getError () {
        return new MinPropertiesError (schema, instance, nonNull(schema.getMinProperties ()));
    }
}

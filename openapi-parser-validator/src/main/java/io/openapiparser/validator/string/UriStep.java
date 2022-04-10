/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.string;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.ValidationMessage;
import io.openapiparser.validator.steps.SimpleStep;

public class UriStep extends SimpleStep {

    public UriStep (JsonSchema schema, JsonInstance instance) {
        super (schema, instance);
    }

    @Override
    protected ValidationMessage getError () {
        return new UriError (schema, instance);
    }
}

/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.steps;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;

public class ValidateStep extends CompositeStep {
    private final JsonSchema schema;
    private final JsonInstance instance;

    public ValidateStep (JsonSchema schema, JsonInstance instance) {
        this.schema = schema;
        this.instance = instance;
    }

    @Override
    public String toString () {
        return String.format ("%s (instance: %s), (schema: %s)", isValid () ? "valid" : "invalid",
            schema.toString (),
            instance.toString ());
    }
}

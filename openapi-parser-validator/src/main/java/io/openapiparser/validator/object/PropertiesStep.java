/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.object;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.steps.CompositeStep;

public class PropertiesStep extends CompositeStep {
    private final JsonSchema schema;
    private final JsonInstance instance;

    public PropertiesStep (JsonSchema schema, JsonInstance instance) {
        this.schema = schema;
        this.instance = instance;
    }

    @Override
    public String toString () {
        return String.format ("%s (instance: %s), (schema: %s)", isValid () ? "valid" : "invalid",
            instance.toString (),
            schema.toString ());
    }
}

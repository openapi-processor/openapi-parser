/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.steps;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.schema.Keywords;

public class DynamicRefStep extends CompositeStep {
    private final JsonSchema schema;
    protected final JsonInstance instance;

    public DynamicRefStep (JsonSchema schema, JsonInstance instance) {
        this.schema = schema;
        this.instance = instance;
    }

    public String toString () {
        return String.format ("%s (instance: %s), (schema: %s)",
            isValid () ? "valid" : "invalid",
            instance.getLocation (),
            schema.getLocation ().append (Keywords.DYNAMIC_REF));
    }
}

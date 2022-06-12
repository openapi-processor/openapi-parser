/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.steps;

import io.openapiparser.schema.JsonSchema;

public class SchemaRefStep extends CompositeStep {
    private final JsonSchema schema;

    public SchemaRefStep (JsonSchema schema) {
        this.schema = schema;
    }

    @Override
    public String toString () {
        return String.format ("%s", schema.toString ());
    }
}

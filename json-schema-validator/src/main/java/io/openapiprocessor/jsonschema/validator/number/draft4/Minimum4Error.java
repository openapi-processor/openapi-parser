/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.number.draft4;

import io.openapiprocessor.jsonschema.schema.JsonInstance;
import io.openapiprocessor.jsonschema.schema.JsonSchema;
import io.openapiprocessor.jsonschema.validator.ValidationMessage;

/**
 * Created by {@link Minimum4Step}.
 */
public class Minimum4Error extends ValidationMessage {
    public Minimum4Error (JsonSchema schema, JsonInstance instance) {
        super (schema, instance,
            "minimum",
            String.format ("the value should be %s than %s",
                schema.getExclusiveMinimumB () ? "greater" : "greater or equal",
                schema.getMinimum ()));
    }
}

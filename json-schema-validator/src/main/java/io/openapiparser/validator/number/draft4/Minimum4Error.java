/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.number.draft4;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.ValidationMessage;

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

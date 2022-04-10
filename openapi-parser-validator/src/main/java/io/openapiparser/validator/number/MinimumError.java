/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.number;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.ValidationMessage;

/**
 * Created by {@link MinimumStep}.
 */
public class MinimumError extends ValidationMessage {
    public MinimumError (JsonSchema schema, JsonInstance instance) {
        super (schema, instance, String.format ("the value should be %s than %s",
            schema.getExclusiveMinimum () ? "greater" : "greater or equal",
            schema.getMinimum ().toString ()));
    }
}

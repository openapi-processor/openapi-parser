/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.number;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.ValidationMessage;

/**
 * Created by {@link MaximumStep}.
 */
public class MaximumError extends ValidationMessage {

    public MaximumError (JsonSchema schema, JsonInstance instance) {
        super (schema, instance, String.format ("the value should be %s than %s",
            schema.getExclusiveMaximum () ? "less" : "less or equal", schema.getMaximum ().toString ()));
    }
}

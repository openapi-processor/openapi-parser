/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.number;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.ValidationMessage;

/**
 * Created by {@link ExclusiveMinimumStep}.
 */
public class ExclusiveMinimumError extends ValidationMessage {
    public ExclusiveMinimumError (JsonSchema schema, JsonInstance instance) {
        super (schema, instance, String.format ("the value should be greater than %s",
            schema.getExclusiveMinimum ()));
    }
}

/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.string;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.ValidationMessage;

/**
 * Created by {@link MaxLengthStep}.
 */
public class MaxLengthError extends ValidationMessage {
    public MaxLengthError (JsonSchema schema, JsonInstance instance) {
        super (schema, instance,
            "maxLength",
            String.format ("the length should be less or equal to %s",
                schema.getMaxLength ()));
    }
}

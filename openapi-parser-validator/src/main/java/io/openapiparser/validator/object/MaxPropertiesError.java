/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.object;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.ValidationMessage;

/**
 * Created by {@link MaxPropertiesStep}.
 */
public class MaxPropertiesError extends ValidationMessage {

    public MaxPropertiesError (JsonSchema schema, JsonInstance instance, int size) {
        super (schema, instance, String.format ("the size should be less or equal to %d", size));
    }
}

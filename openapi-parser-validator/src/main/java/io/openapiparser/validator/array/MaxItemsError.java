/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.array;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.ValidationMessage;

/**
 * Created by {@link MaxItemsStep}.
 */
public class MaxItemsError extends ValidationMessage {

    public MaxItemsError (JsonSchema schema, JsonInstance instance, int size) {
        super (schema, instance,
            "maxItems",
            String.format ("should be less or equal to %d", size));
    }
}

/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.array;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.ValidationMessage;

/**
 * Created by {@link Items}.
 */
public class ItemsSizeError extends ValidationMessage {
    public ItemsSizeError (JsonSchema schema, JsonInstance instance) {
        super (schema, instance,
            "items",
            String.format ("the number of items should be less or equal to %d",
                schema.getItems ().size ()));
    }
}

/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.array;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.ValidationMessage;

/**
 * Created by {@link ContainsStep}.
 */
public class MaxContainsError extends ValidationMessage {

    public MaxContainsError (JsonSchema schema, JsonInstance instance, int size) {
        super (schema, instance,
            "maxContains",
            String.format ("should contain at maximum %d items", size));
    }
}

/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.object;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.ValidationMessage;

/**
 * Created by {@link MinPropertiesStep}.
 */
public class MinPropertiesError extends ValidationMessage {

    public MinPropertiesError (JsonSchema schema, JsonInstance instance, int size) {
        super (schema, instance, "minProperties",
            String.format ("the size should be greater or equal to %d", size));
    }
}

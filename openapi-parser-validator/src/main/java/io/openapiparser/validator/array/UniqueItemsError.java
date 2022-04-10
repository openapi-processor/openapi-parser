/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.array;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.ValidationMessage;

/**
 * Created by {@link UniqueItemsStep}.
 */
public class UniqueItemsError extends ValidationMessage {

    public UniqueItemsError (JsonSchema schema, JsonInstance instance) {
        super (schema, instance, "the items should be unique");
    }
}

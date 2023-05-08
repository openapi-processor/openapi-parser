/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.array;

import io.openapiprocessor.jsonschema.schema.JsonInstance;
import io.openapiprocessor.jsonschema.schema.JsonSchema;
import io.openapiprocessor.jsonschema.validator.ValidationMessage;

/**
 * Created by {@link UniqueItemsStep}.
 */
public class UniqueItemsError extends ValidationMessage {

    public UniqueItemsError (JsonSchema schema, JsonInstance instance) {
        super (schema, instance,
            "items",
            "the items should be unique");
    }
}

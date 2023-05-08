/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.array;

import io.openapiprocessor.jsonschema.schema.JsonInstance;
import io.openapiprocessor.jsonschema.schema.JsonSchema;
import io.openapiprocessor.jsonschema.validator.ValidationMessage;

/**
 * Created by {@link Items}.
 */
@Deprecated
public class ItemsSizeError extends ValidationMessage {
    public ItemsSizeError (JsonSchema schema, JsonInstance instance) {
        super (schema, instance,
            "items",
            String.format ("the number of items should be less or equal to %d",
                schema.getItems ().size ()));
    }
}

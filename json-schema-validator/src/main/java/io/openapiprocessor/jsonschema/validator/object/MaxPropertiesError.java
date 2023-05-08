/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.object;

import io.openapiprocessor.jsonschema.schema.JsonInstance;
import io.openapiprocessor.jsonschema.schema.JsonSchema;
import io.openapiprocessor.jsonschema.validator.ValidationMessage;

/**
 * Created by {@link MaxPropertiesStep}.
 */
public class MaxPropertiesError extends ValidationMessage {

    public MaxPropertiesError (JsonSchema schema, JsonInstance instance, int size) {
        super (schema, instance,
            "maxProperties",
            String.format ("the size should be less or equal to %d", size));
    }
}

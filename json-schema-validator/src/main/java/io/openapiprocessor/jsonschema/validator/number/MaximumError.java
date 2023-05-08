/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.number;

import io.openapiprocessor.jsonschema.schema.JsonInstance;
import io.openapiprocessor.jsonschema.schema.JsonSchema;
import io.openapiprocessor.jsonschema.validator.ValidationMessage;

/**
 * Created by {@link MaximumStep}.
 */
public class MaximumError extends ValidationMessage {

    public MaximumError (JsonSchema schema, JsonInstance instance) {
        super (schema, instance,
            "maximum",
            String.format ("the value should be less or equal than %s", schema.getMaximum ()));
    }
}

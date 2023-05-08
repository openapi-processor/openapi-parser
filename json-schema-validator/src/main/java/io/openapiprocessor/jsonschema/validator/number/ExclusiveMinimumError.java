/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.number;

import io.openapiprocessor.jsonschema.schema.JsonInstance;
import io.openapiprocessor.jsonschema.schema.JsonSchema;
import io.openapiprocessor.jsonschema.validator.ValidationMessage;

/**
 * Created by {@link ExclusiveMinimumStep}.
 */
public class ExclusiveMinimumError extends ValidationMessage {
    public ExclusiveMinimumError (JsonSchema schema, JsonInstance instance) {
        super (schema, instance,
            "exclusiveMinimum",
            String.format ("the value should be greater than %s", schema.getExclusiveMinimum ()));
    }
}

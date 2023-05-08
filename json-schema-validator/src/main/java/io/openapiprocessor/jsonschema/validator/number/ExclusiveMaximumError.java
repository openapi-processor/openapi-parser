/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.number;

import io.openapiprocessor.jsonschema.schema.JsonInstance;
import io.openapiprocessor.jsonschema.schema.JsonSchema;
import io.openapiprocessor.jsonschema.validator.ValidationMessage;

/**
 * Created by {@link ExclusiveMaximumStep}.
 */
public class ExclusiveMaximumError extends ValidationMessage {

    public ExclusiveMaximumError (JsonSchema schema, JsonInstance instance) {
        super (schema, instance,
            "exclusiveMaximum",
            String.format ("the value should be less than %s", schema.getExclusiveMaximum ()));
    }
}

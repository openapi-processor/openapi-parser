/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.number;

import io.openapiprocessor.jsonschema.schema.JsonInstance;
import io.openapiprocessor.jsonschema.schema.JsonSchema;
import io.openapiprocessor.jsonschema.validator.ValidationMessage;

import static io.openapiprocessor.jsonschema.support.Nullness.nonNull;

/**
 * Created by {@link MultipleOfStep}.
 */
public class MultipleOfError extends ValidationMessage {
    public MultipleOfError (JsonSchema schema, JsonInstance instance) {
        super (schema, instance,
            "multipleOf",
            String.format ("the value should be a multiple of %s",
                nonNull(schema.getMultipleOf ())));
    }
}

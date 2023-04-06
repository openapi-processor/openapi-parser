/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.number;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.ValidationMessage;

import static io.openapiparser.support.Nullness.nonNull;

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

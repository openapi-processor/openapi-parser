/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.array;

import io.openapiprocessor.jsonschema.schema.JsonInstance;
import io.openapiprocessor.jsonschema.schema.JsonSchema;
import io.openapiprocessor.jsonschema.validator.ValidationMessage;

/**
 * Created by {@link ContainsStep}.
 */
public class MinContainsError extends ValidationMessage {

    public MinContainsError (JsonSchema schema, JsonInstance instance, int size) {
        super (schema, instance,
            "minContains",
            String.format ("should contain at minimum %d items", size));
    }
}

/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.string;

import io.openapiprocessor.jsonschema.schema.JsonInstance;
import io.openapiprocessor.jsonschema.schema.JsonSchema;
import io.openapiprocessor.jsonschema.validator.ValidationMessage;

/**
 * Created by {@link MinLengthStep}.
 */
public class MinLengthError extends ValidationMessage {

    public MinLengthError (JsonSchema schema, JsonInstance instance) {
        super (schema, instance,
            "minLength",
            String.format ("the length should be greater or equal to %s",
                schema.getMinLength ()
            ));
    }
}

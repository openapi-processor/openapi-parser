/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.string;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.ValidationMessage;

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

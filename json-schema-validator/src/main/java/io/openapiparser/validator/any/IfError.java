/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.any;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.ValidationMessage;

/**
 * Created by {@link IfStep}.
 */
public class IfError extends ValidationMessage {

    public IfError (JsonSchema schema, JsonInstance instance, String condition) {
        super(schema, instance, "if", String.format ("should validate against '%s' schema", condition));
    }
}

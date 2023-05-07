/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.string;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.ValidationMessage;

/**
 * Created by {@link EmailStep}.
 */
public class EmailError extends ValidationMessage {
    public EmailError (JsonSchema schema, JsonInstance instance) {
        super (schema, instance, "email","should conform to rfc5322");
    }
}

/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.string;

import io.openapiprocessor.jsonschema.schema.JsonInstance;
import io.openapiprocessor.jsonschema.schema.JsonSchema;
import io.openapiprocessor.jsonschema.validator.ValidationMessage;

/**
 * Created by {@link EmailStep}.
 */
public class EmailError extends ValidationMessage {
    public EmailError (JsonSchema schema, JsonInstance instance) {
        super (schema, instance, "email","should conform to rfc5322");
    }
}

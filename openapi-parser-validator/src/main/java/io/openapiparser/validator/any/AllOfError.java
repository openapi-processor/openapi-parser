/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.any;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.ValidationMessage;

import java.util.Collection;

public class AllOfError extends ValidationMessage {

    public AllOfError (
        JsonSchema schema,
        JsonInstance instance,
        Collection<ValidationMessage> messages
    ) {
        super (schema, instance, "allOf", "should validate against all schemas", messages);
    }
}

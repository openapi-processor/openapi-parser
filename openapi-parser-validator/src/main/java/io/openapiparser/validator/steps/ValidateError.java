/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.steps;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.ValidationMessage;

import java.util.Collection;

/**
 * Created by {@link SchemaStep}.
 */
// todo what is the purpose of this class?
public class ValidateError extends ValidationMessage {

    public ValidateError (
        JsonSchema schema,
        JsonInstance instance,
        Collection<ValidationMessage> messages)
    {
        super (schema, instance, "", messages);
    }
}

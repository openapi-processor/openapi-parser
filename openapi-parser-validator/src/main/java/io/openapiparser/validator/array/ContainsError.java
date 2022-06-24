/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.array;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.ValidationMessage;

import java.util.Collection;

/**
 * Created by {@link ContainsStep}.
 */
public class ContainsError extends ValidationMessage {

    public ContainsError (
        JsonSchema schema,
        JsonInstance instance,
        Collection<ValidationMessage> messages
    ) {
        super (schema, instance,
            "contains",
            "should contain at least one matching element",
            messages);
    }
}

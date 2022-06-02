/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.any;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.ValidationMessage;

import java.util.Collection;

/**
 * Created by {@link IfStep}.
 */
public class IfError extends ValidationMessage {

    public IfError (
        JsonSchema schema,
        JsonInstance instance,
        String condition,
        Collection<ValidationMessage> messages
    ) {
        super(schema, instance,
            String.format ("should validate against '%s' schema", condition), messages);
    }
}

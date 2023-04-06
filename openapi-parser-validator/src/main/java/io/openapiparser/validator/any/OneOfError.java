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
 * Created by {@link io.openapiparser.validator.any.OneOfStep}.
 */
public class OneOfError extends ValidationMessage {

    public OneOfError (
        JsonSchema schema,
        JsonInstance instance,
        Collection<ValidationMessage> messages
    ) {
        super (schema, instance,
            "oneOf",
            "should validate against exactly one schema", messages);
    }
}

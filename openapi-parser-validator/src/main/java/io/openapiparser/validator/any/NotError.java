/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.any;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.ValidationMessage;

/**
 * Created by {@link io.openapiparser.validator.any.NotStep}.
 */
public class NotError extends ValidationMessage {
    public NotError (JsonSchema schema, JsonInstance instance) {
        super(schema, instance, "not", "should not validate to true");
    }
}

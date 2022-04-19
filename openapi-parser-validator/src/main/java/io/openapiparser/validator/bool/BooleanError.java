/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.bool;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.ValidationMessage;

/**
 * Created by {@link BooleanStep}.
 */
public class BooleanError extends ValidationMessage {

    public BooleanError (JsonSchema schema, JsonInstance instance) {
        super (schema, instance, "should not validate with false");
    }
}

/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.any;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.ValidationMessage;

/**
 * Created by {@link ConstStep}.
 */
public class ConstError extends ValidationMessage {

    public ConstError (JsonSchema schema, JsonInstance instance) {
        super (schema, instance, "should be equal to constant");
    }
}

/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.object;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.ValidationMessage;

/**
 * Created by {@link RequiredStep}.
 */
public class RequiredError extends ValidationMessage {

    public RequiredError (JsonSchema schema, JsonInstance instance, String propertyName) {
        super (schema, instance, String.format ("should have a property '%s'", propertyName));
    }
}

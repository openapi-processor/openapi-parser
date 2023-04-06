/*
 * Copyright 2023 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.object;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.ValidationMessage;

/**
 * Created by {@link PropertyInvalidStep}.
 */
public class PropertyInvalidError extends ValidationMessage {

    public PropertyInvalidError (JsonSchema schema, JsonInstance instance, String propertyName) {
        super (schema, instance,
            propertyName,
            "property is invalid");
    }
}

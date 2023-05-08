/*
 * Copyright 2023 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.object;

import io.openapiprocessor.jsonschema.schema.JsonInstance;
import io.openapiprocessor.jsonschema.schema.JsonSchema;
import io.openapiprocessor.jsonschema.validator.ValidationMessage;

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

/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.object;

import io.openapiprocessor.jsonschema.schema.JsonInstance;
import io.openapiprocessor.jsonschema.schema.JsonSchema;
import io.openapiprocessor.jsonschema.validator.ValidationMessage;

/**
 * Created by {@link RequiredStep}.
 */
public class RequiredError extends ValidationMessage {

    public RequiredError (JsonSchema schema, JsonInstance instance, String propertyName) {
        super (schema, instance,
            "required",
            String.format ("should have a property '%s'", propertyName));
    }
}

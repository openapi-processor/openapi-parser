/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.object;

import io.openapiprocessor.jsonschema.schema.JsonInstance;
import io.openapiprocessor.jsonschema.schema.JsonSchema;
import io.openapiprocessor.jsonschema.validator.ValidationMessage;

/**
 * Created by {@link MinPropertiesStep}.
 */
public class MinPropertiesError extends ValidationMessage {

    public MinPropertiesError (JsonSchema schema, JsonInstance instance, Integer size) {
        super (schema, instance, "minProperties",
            String.format ("the size should be greater or equal to %d", size));
    }
}

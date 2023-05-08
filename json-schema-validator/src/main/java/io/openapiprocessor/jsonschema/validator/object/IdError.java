/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.object;

import io.openapiprocessor.jsonschema.schema.JsonInstance;
import io.openapiprocessor.jsonschema.schema.JsonSchema;
import io.openapiprocessor.jsonschema.validator.ValidationMessage;

/**
 * Created by {@link IdStep}.
 */
public class IdError extends ValidationMessage {

    public IdError (JsonSchema schema, JsonInstance instance) {
        super (schema, instance,
            "id",
            "the value should be an absolute uri without fragment");
    }
}

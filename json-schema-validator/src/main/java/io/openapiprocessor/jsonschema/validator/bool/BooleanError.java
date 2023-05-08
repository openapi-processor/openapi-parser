/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.bool;

import io.openapiprocessor.jsonschema.schema.JsonInstance;
import io.openapiprocessor.jsonschema.schema.JsonSchema;
import io.openapiprocessor.jsonschema.validator.ValidationMessage;

/**
 * Created by {@link BooleanStep}.
 */
public class BooleanError extends ValidationMessage {

    public BooleanError (JsonSchema schema, JsonInstance instance) {
        super (schema, instance, "boolean",
            String.format ("the value does not validate against the '%s' schema", schema.isTrue () ? "true" : "false"));
    }
}

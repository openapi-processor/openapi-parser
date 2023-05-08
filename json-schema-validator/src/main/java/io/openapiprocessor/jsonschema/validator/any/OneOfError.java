/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.any;

import io.openapiprocessor.jsonschema.schema.JsonInstance;
import io.openapiprocessor.jsonschema.schema.JsonSchema;
import io.openapiprocessor.jsonschema.validator.ValidationMessage;

/**
 * Created by {@link OneOfStep}.
 */
public class OneOfError extends ValidationMessage {

    public OneOfError (JsonSchema schema, JsonInstance instance) {
        super (schema, instance, "oneOf", "should validate against exactly one schema");
    }
}

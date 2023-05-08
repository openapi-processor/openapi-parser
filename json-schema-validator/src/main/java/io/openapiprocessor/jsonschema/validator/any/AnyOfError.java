/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.any;

import io.openapiprocessor.jsonschema.schema.JsonInstance;
import io.openapiprocessor.jsonschema.schema.JsonSchema;
import io.openapiprocessor.jsonschema.validator.ValidationMessage;

/**
 * Created by {@link AnyOfStep}.
 */
public class AnyOfError extends ValidationMessage {

    public AnyOfError (JsonSchema schema, JsonInstance instance) {
        super (schema, instance, "anyOf", "should validate against any schema");
    }
}

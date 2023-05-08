/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.any;

import io.openapiprocessor.jsonschema.schema.JsonInstance;
import io.openapiprocessor.jsonschema.schema.JsonSchema;
import io.openapiprocessor.jsonschema.validator.ValidationMessage;

/**
 * Created by {@link IfStep}.
 */
public class IfError extends ValidationMessage {

    public IfError (JsonSchema schema, JsonInstance instance, String condition) {
        super(schema, instance, "if", String.format ("should validate against '%s' schema", condition));
    }
}

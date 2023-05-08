/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.string;

import io.openapiprocessor.jsonschema.schema.JsonInstance;
import io.openapiprocessor.jsonschema.schema.JsonSchema;
import io.openapiprocessor.jsonschema.validator.ValidationMessage;

/**
 * Created by {@link DateTimeStep}.
 */
public class DateTimeError extends ValidationMessage {
    public DateTimeError (JsonSchema schema, JsonInstance instance) {
        super (schema, instance, "datetime", "should conform to ISO 8601");
    }
}

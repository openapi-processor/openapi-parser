/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.string;

import io.openapiprocessor.jsonschema.schema.JsonInstance;
import io.openapiprocessor.jsonschema.schema.JsonSchema;
import io.openapiprocessor.jsonschema.validator.ValidationMessage;

/**
 * Created by {@link RegexStep}.
 */
public class RegexError extends ValidationMessage {

    public RegexError (JsonSchema schema, JsonInstance instance) {
        super (schema, instance, "regex", "should be a regular expression");
    }
}

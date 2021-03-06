/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.string;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.ValidationMessage;

/**
 * Created by {@link PatternStep}.
 */
public class PatternError extends ValidationMessage {

    public PatternError (JsonSchema schema, JsonInstance instance) {
        super (schema, instance, String.format ("should match the regular expression '%s'",
            schema.getPattern ()));
    }
}

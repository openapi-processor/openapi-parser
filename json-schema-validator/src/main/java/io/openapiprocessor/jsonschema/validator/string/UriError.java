/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.string;

import io.openapiprocessor.jsonschema.schema.JsonInstance;
import io.openapiprocessor.jsonschema.schema.JsonSchema;
import io.openapiprocessor.jsonschema.validator.ValidationMessage;

/**
 * Created by {@link UriStep}.
 */
public class UriError extends ValidationMessage {
    public UriError (JsonSchema schema, JsonInstance instance) {
        super (schema, instance, "uri", "should conform to rfc3968");
    }
}

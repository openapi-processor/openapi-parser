/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.string;

import io.openapiprocessor.jsonschema.schema.JsonInstance;
import io.openapiprocessor.jsonschema.schema.JsonSchema;
import io.openapiprocessor.jsonschema.validator.ValidationMessage;

/**
 * Created by {@link HostnameStep}.
 */
public class HostnameError extends ValidationMessage {
    public HostnameError (JsonSchema schema, JsonInstance instance) {
        super (schema, instance, "hostname", "should conform to rfc1034");
    }
}

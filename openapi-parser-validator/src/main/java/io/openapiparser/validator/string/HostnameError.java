/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.string;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.ValidationMessage;

/**
 * Created by {@link HostnameStep}.
 */
public class HostnameError extends ValidationMessage {
    public HostnameError (JsonSchema schema, JsonInstance instance) {
        super (schema, instance, "should conform to rfc1034");
    }
}

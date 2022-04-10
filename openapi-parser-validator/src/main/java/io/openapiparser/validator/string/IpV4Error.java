/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.string;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.ValidationMessage;

/**
 * Created by {@link IpV4Step}.
 */
public class IpV4Error extends ValidationMessage {
    public IpV4Error (JsonSchema schema, JsonInstance instance) {
        super (schema, instance, "should conform to rfc2673");
    }
}

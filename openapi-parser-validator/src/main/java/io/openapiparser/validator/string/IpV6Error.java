/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.string;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.ValidationMessage;

/**
 * Created by {@link IpV6}.
 */
public class IpV6Error extends ValidationMessage {
    public IpV6Error (JsonSchema schema, JsonInstance instance) {
        super (schema, instance, "ipv6", "should conform to rfc2373");
    }
}

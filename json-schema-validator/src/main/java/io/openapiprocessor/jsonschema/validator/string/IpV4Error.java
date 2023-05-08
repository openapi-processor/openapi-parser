/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.string;

import io.openapiprocessor.jsonschema.schema.JsonInstance;
import io.openapiprocessor.jsonschema.schema.JsonSchema;
import io.openapiprocessor.jsonschema.validator.ValidationMessage;

/**
 * Created by {@link IpV4Step}.
 */
public class IpV4Error extends ValidationMessage {
    public IpV4Error (JsonSchema schema, JsonInstance instance) {
        super (schema, instance, "ipv4", "should conform to rfc2673");
    }
}

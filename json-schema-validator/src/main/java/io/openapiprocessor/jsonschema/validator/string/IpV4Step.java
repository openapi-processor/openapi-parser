/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.string;

import io.openapiprocessor.jsonschema.schema.JsonInstance;
import io.openapiprocessor.jsonschema.schema.JsonSchema;
import io.openapiprocessor.jsonschema.schema.Keywords;
import io.openapiprocessor.jsonschema.validator.ValidationMessage;
import io.openapiprocessor.jsonschema.validator.steps.SimpleStep;

public class IpV4Step extends SimpleStep {

    public IpV4Step (JsonSchema schema, JsonInstance instance) {
        super(schema, instance, Keywords.FORMAT);
    }

    @Override
    protected ValidationMessage getError () {
        return new IpV4Error (schema, instance);
    }
}

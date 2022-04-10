/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.object;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.ValidationMessage;

/**
 * Created by {@link io.openapiparser.validator.object.Properties}.
 */
public class PropertiesError extends ValidationMessage {

    public PropertiesError (JsonSchema schema, JsonInstance instance) {
        super (schema, instance, "should not have an invalid property");
    }
}

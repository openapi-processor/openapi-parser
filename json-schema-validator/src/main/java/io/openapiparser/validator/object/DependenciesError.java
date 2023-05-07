/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.object;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.ValidationMessage;

/**
 * Created by {@link io.openapiparser.validator.object.DependenciesStep}.
 */
public class DependenciesError extends ValidationMessage {

    public DependenciesError (JsonSchema schema, JsonInstance instance, String propertyName) {
        super (schema, instance,
            "dependencies",
            String.format ("should have dependency property '%s'", propertyName));
    }
}

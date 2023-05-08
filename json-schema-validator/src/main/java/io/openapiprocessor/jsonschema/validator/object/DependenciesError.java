/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.object;

import io.openapiprocessor.jsonschema.schema.JsonInstance;
import io.openapiprocessor.jsonschema.schema.JsonSchema;
import io.openapiprocessor.jsonschema.validator.ValidationMessage;

/**
 * Created by {@link DependenciesStep}.
 */
public class DependenciesError extends ValidationMessage {

    public DependenciesError (JsonSchema schema, JsonInstance instance, String propertyName) {
        super (schema, instance,
            "dependencies",
            String.format ("should have dependency property '%s'", propertyName));
    }
}

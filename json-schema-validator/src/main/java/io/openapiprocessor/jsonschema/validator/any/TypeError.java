/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.any;

import io.openapiprocessor.jsonschema.schema.JsonInstance;
import io.openapiprocessor.jsonschema.schema.JsonSchema;
import io.openapiprocessor.jsonschema.validator.ValidationMessage;

import java.util.Collection;

/**
 * Created by {@link TypeStep}.
 */
public class TypeError extends ValidationMessage {

    public TypeError (JsonSchema schema, JsonInstance instance, Collection<String> types) {
        super (schema, instance,
            "type",
            String.format ("the value should be any of [%s]", String.join (", ", types)));
    }
}

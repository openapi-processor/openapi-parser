/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.any;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.ValidationMessage;

import java.util.Collection;

/**
 * Created by {@link TypeStep}.
 */
public class TypeError extends ValidationMessage {

    public TypeError (JsonSchema schema, JsonInstance instance, Collection<String> types) {
        super (schema, instance, String.format ("the type should be any of [%s]",
            String.join (", ", types)));
    }
}

/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.number.draft4;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.ValidationMessage;

import static io.openapiparser.support.Nullness.nonNull;

/**
 * Created by {@link Maximum4Step}.
 */
public class Maximum4Error extends ValidationMessage {

    public Maximum4Error (JsonSchema schema, JsonInstance instance) {
        super (schema, instance,
            "maximum",
            String.format ("the value should be %s than %s",
                schema.getExclusiveMaximumB () ? "less" : "less or equal",
                nonNull (schema.getMaximum ())));
    }
}

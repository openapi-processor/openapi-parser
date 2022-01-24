/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.number;

import io.openapiparser.validator.ValidationMessage;

/**
 * Created by {@link MaximumExclusiveMaximum}.
 */
public class MaximumError extends ValidationMessage {
    public MaximumError (String path, Number maximum, Boolean exclusive) {
        super (path, String.format ("the value should be %s than %s",
            exclusive ? "less" : "les or equal", maximum.toString ()));
    }
}

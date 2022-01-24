/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.number;

import io.openapiparser.validator.ValidationMessage;

/**
 * Created by {@link Minimum}.
 */
public class MinimumError extends ValidationMessage {
    public MinimumError (String path, Number minimum, Boolean exclusive) {
        super (path, String.format ("the value should be %s than %s",
            exclusive ? "greater" : "greater or equal", minimum.toString ()));
    }
}

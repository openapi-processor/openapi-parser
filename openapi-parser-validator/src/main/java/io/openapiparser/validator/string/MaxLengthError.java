/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.string;

import io.openapiparser.validator.ValidationMessage;

/**
 * Created by {@link MaxLength}.
 */
public class MaxLengthError extends ValidationMessage {
    public MaxLengthError (String path, Integer maxLength) {
        super (path, String.format ("the length should be less or equal to %s", maxLength));
    }
}

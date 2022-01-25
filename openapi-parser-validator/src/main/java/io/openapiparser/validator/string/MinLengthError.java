/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.string;

import io.openapiparser.validator.ValidationMessage;

/**
 * Created by {@link MinLength}.
 */
public class MinLengthError extends ValidationMessage {
    public MinLengthError (String path, Integer minLength) {
        super (path, String.format ("the length should be greater or equal to %s", minLength));
    }
}

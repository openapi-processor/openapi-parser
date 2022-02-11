/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.object;

import io.openapiparser.validator.ValidationMessage;

/**
 * Created by {@link MinProperties}.
 */
public class MinPropertiesError extends ValidationMessage {
    public MinPropertiesError (String path, int size) {
        super (path, String.format ("the size should be greater or equal to %d", size));
    }
}

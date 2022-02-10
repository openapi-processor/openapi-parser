/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.object;

import io.openapiparser.validator.ValidationMessage;
import io.openapiparser.validator.array.MaxItems;

/**
 * Created by {@link MaxItems}.
 */
public class MaxPropertiesError extends ValidationMessage {
    public MaxPropertiesError (String path, int size) {
        super (path, String.format ("the size should be less or equal to %d", size));
    }
}

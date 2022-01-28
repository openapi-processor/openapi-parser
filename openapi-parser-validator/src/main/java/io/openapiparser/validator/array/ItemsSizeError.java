/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.array;

import io.openapiparser.validator.ValidationMessage;

/**
 * Created by {@link Items}.
 */
public class ItemsSizeError extends ValidationMessage {
    public ItemsSizeError (String path, int size) {
        super (path, String.format ("the size should be less or equal to %d", size));
    }
}

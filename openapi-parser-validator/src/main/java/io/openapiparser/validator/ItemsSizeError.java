/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator;

/**
 * draft4 - 5.9
 */
public class ItemsSizeError extends ValidationMessage {
    public ItemsSizeError (String path, int size) {
        super (path, String.format ("the size is not less or equal to %d", size));
    }
}

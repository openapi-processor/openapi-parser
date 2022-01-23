/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.messages;

/**
 * draft4 - 5.9
 */
public class MinItemsError extends ValidationMessage {
    public MinItemsError (String path, int size) {
        super (path, String.format ("the size is not greater or equal to %d", size));
    }
}

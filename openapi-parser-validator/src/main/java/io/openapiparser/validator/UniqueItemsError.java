/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator;

/**
 * draft4 - 5.12
 */
public class UniqueItemsError extends ValidationMessage {
    public UniqueItemsError (String path) {
        super (path, "the items are not unique");
    }
}

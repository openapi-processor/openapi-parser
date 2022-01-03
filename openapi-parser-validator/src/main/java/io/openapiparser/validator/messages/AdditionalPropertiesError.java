/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.messages;

/**
 * draft4 - 5.18
 */
public class AdditionalPropertiesError extends ValidationMessage {
    public AdditionalPropertiesError (String path) {
        super (path, "disallowed additional property");
    }
}

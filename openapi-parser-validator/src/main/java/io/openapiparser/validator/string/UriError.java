/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.string;

import io.openapiparser.validator.ValidationMessage;

/**
 * Created by {@link Uri}.
 */
public class UriError extends ValidationMessage {
    public UriError (String path) {
        super (path, "should conform to rfc3968");
    }
}

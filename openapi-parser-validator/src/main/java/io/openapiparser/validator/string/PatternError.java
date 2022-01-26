/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.string;

import io.openapiparser.validator.ValidationMessage;

/**
 * Created by {@link Pattern}.
 */
public class PatternError extends ValidationMessage {
    public PatternError (String path, String regex) {
        super (path, String.format ("should match the regular expression %s", regex));
    }
}

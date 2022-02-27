/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.string;

import io.openapiparser.validator.ValidationMessage;

/**
 * Created by {@link DateTime}.
 */
public class DateTimeError extends ValidationMessage {
    public DateTimeError (String path) {
        super (path, "should conform to ISO 8601");
    }
}

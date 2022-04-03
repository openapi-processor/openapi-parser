/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.object;

import io.openapiparser.validator.ValidationMessage;

/**
 * Created by {@link Required}.
 */
public class RequiredError extends ValidationMessage {
    public RequiredError (String path, String property) {
        super (path, String.format ("should have a property '%s'", property));
    }
}

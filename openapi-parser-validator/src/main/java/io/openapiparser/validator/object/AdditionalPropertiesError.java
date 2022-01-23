/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.object;

import io.openapiparser.validator.ValidationMessage;

/**
 * Created by {@link todo}.
 */
public class AdditionalPropertiesError extends ValidationMessage {
    public AdditionalPropertiesError (String path) {
        super (path, "disallowed additional property");
    }
}

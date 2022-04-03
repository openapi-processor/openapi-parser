/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.object;

import io.openapiparser.validator.ValidationMessage;

/**
 * Created by {@link io.openapiparser.validator.Validator}.
 */
public class PropertiesError extends ValidationMessage {
    public PropertiesError (String scope, String path) {
        super (scope, path, "invalid property");
    }
}

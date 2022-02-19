/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.any;

import io.openapiparser.validator.ValidationMessage;

/**
 * Created by {@link io.openapiparser.validator.Validator}.
 */
public class OneOfError extends ValidationMessage {
    public OneOfError (String path) {
        super (path, String.format ("should validate against exactly one schema of %s", path));
    }
}

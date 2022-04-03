/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.steps;

import io.openapiparser.validator.ValidationMessage;

public class ErrorStep extends SimpleStep {

    public ErrorStep (ValidationMessage message) {
        super(message);
    }
}

/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.string;

import io.openapiparser.validator.ValidationMessage;
import io.openapiparser.validator.steps.SimpleStep;

public class MaxLengthStep extends SimpleStep {

    public MaxLengthStep () {
        super ();
    }

    public MaxLengthStep (ValidationMessage message) {
        super(message);
    }
}

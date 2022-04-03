/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.string;

import io.openapiparser.validator.ValidationMessage;
import io.openapiparser.validator.steps.SimpleStep;

public class MinLengthStep extends SimpleStep {

    public MinLengthStep () {
        super ();
    }

    public MinLengthStep (ValidationMessage message) {
        super(message);
    }
}

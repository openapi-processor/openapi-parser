/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.number;

import io.openapiparser.validator.ValidationMessage;
import io.openapiparser.validator.steps.SimpleStep;

public class MaximumStep extends SimpleStep {

    public MaximumStep () {
        super ();
    }

    public MaximumStep (ValidationMessage message) {
        super (message);
    }
}

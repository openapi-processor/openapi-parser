/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.any;

import io.openapiparser.validator.ValidationMessage;
import io.openapiparser.validator.steps.SimpleStep;

public class EnumStep extends SimpleStep {

    public EnumStep () {
        super ();
    }

    public EnumStep (ValidationMessage message) {
        super(message);
    }
}

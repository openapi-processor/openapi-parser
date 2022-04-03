/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.object;

import io.openapiparser.validator.ValidationMessage;
import io.openapiparser.validator.steps.SimpleStep;

public class MaxPropertiesStep extends SimpleStep {

    public MaxPropertiesStep () {
        super ();
    }

    public MaxPropertiesStep (ValidationMessage message) {
        super(message);
    }
}

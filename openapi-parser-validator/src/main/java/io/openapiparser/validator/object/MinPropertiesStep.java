/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.object;

import io.openapiparser.validator.ValidationMessage;
import io.openapiparser.validator.steps.SimpleStep;

public class MinPropertiesStep extends SimpleStep {

    public MinPropertiesStep () {
        super ();
    }

    public MinPropertiesStep (ValidationMessage message) {
        super(message);
    }
}

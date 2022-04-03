/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.string;

import io.openapiparser.validator.ValidationMessage;
import io.openapiparser.validator.steps.SimpleStep;

public class IpV4Step extends SimpleStep {

    public IpV4Step () {
        super ();
    }

    public IpV4Step (ValidationMessage message) {
        super(message);
    }
}

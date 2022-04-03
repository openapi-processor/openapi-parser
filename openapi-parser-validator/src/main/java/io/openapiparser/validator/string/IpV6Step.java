/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.string;

import io.openapiparser.validator.ValidationMessage;
import io.openapiparser.validator.steps.SimpleStep;

public class IpV6Step extends SimpleStep {

    public IpV6Step () {
        super ();
    }

    public IpV6Step (ValidationMessage message) {
        super(message);
    }
}

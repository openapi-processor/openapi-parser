/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.bool;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.steps.ValidationStep;

/**
 * validates boolean. Since Draft 6
 */
public class Boolean {

    public void validate (JsonSchema schema, JsonInstance instance, ValidationStep parentStep) {
        BooleanStep step =  new BooleanStep (schema, instance);

        boolean value = schema.getBoolean ();
        if (!value) {
            step.setInvalid ();
        }

        parentStep.add (step);
    }
}

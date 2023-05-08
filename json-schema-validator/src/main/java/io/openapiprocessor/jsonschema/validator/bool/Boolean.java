/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.bool;

import io.openapiprocessor.jsonschema.schema.JsonInstance;
import io.openapiprocessor.jsonschema.schema.JsonSchema;
import io.openapiprocessor.jsonschema.validator.steps.ValidationStep;

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

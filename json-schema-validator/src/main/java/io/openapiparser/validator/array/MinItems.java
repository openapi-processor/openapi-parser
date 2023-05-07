/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.array;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.steps.ValidationStep;

import java.util.Collection;

import static io.openapiparser.support.Nullness.nonNull;

/**
 * validates minItems. Since Draft 4.
 */
public class MinItems {

    public void validate (JsonSchema schema, JsonInstance instance, ValidationStep parentStep) {
        MinItemsStep step = new MinItemsStep (schema, instance);

        Collection<Object> instanceValue = getInstanceValue (instance);
        int minItems = schema.getMinItems ();
        if (instanceValue.size () < minItems) {
            step.setInvalid ();
        }

        parentStep.add (step);
    }

    private Collection<Object> getInstanceValue (JsonInstance instance) {
        return nonNull (instance.asCollection ());
    }
}

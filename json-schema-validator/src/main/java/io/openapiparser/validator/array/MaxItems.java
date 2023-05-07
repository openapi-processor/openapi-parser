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
 * validates maxItems. Since Draft 4.
 */
public class MaxItems {

    public void validate (JsonSchema schema, JsonInstance instance, ValidationStep parentStep) {
        Collection<Object> instanceValue = getInstanceValue (instance);
        Integer maxItems = schema.getMaxItems ();
        if (maxItems == null)
            return;

        MaxItemsStep step = new MaxItemsStep (schema, instance);

        if (instanceValue.size () > maxItems)
            step.setInvalid ();

        parentStep.add (step);
    }

    private Collection<Object> getInstanceValue (JsonInstance instance) {
        return nonNull (instance.asCollection ());
    }
}

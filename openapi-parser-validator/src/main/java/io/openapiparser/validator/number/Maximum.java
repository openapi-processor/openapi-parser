/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.number;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.steps.ValidationStep;

import java.math.BigDecimal;

import static io.openapiparser.support.Nullness.nonNull;

/**
 * maximum. Since Draft 6.
 */
public class Maximum {

    public void validate (JsonSchema schema, JsonInstance instance, ValidationStep parentStep) {
        Number maximum = schema.getMaximum ();

        if (maximum == null)
            return;

        MaximumStep step = new MaximumStep (schema, instance);

        boolean valid = compareTo (instance, maximum) <= 0;
        if (!valid) {
            step.setInvalid ();
        }

        parentStep.add (step);
    }

    private int compareTo (JsonInstance instance, Number maximum) {
        return new BigDecimal (getInstanceValue (instance).toString ())
            .compareTo (new BigDecimal (maximum.toString ()));
    }

    private Number getInstanceValue (JsonInstance instance) {
        return nonNull (instance.asNumber ());
    }
}

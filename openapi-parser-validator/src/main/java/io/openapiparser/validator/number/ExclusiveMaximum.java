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
 * validates exclusiveMaximum. Since Draft 6.
 */
public class ExclusiveMaximum {

    public void validate (JsonSchema schema, JsonInstance instance, ValidationStep parentStep) {
        Number maximum = schema.getExclusiveMaximum ();

        if (maximum == null)
            return;

        ExclusiveMaximumStep step = new ExclusiveMaximumStep (schema, instance);

        boolean valid = compareTo (instance, maximum) < 0;
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

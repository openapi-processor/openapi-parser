/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.number;

import io.openapiprocessor.jsonschema.schema.JsonInstance;
import io.openapiprocessor.jsonschema.schema.JsonSchema;
import io.openapiprocessor.jsonschema.validator.steps.ValidationStep;

import java.math.BigDecimal;

import static io.openapiprocessor.jsonschema.support.Nullness.nonNull;

/**
 * validates minimum. Since Draft 6.
 */
public class Minimum {

    public void validate (JsonSchema schema, JsonInstance instance, ValidationStep parentStep) {
        Number minimum = schema.getMinimum ();

        if (minimum == null)
            return;

        MinimumStep step = new MinimumStep (schema, instance);

        boolean valid = compareTo (instance, minimum) >= 0;
        if (!valid) {
            step.setInvalid ();
        }

        parentStep.add (step);
    }

    private int compareTo (JsonInstance instance, Number minimum) {
        return new BigDecimal (getInstanceValue (instance).toString ())
            .compareTo (new BigDecimal (minimum.toString ()));
    }

    private Number getInstanceValue (JsonInstance instance) {
        return nonNull (instance.asNumber ());
    }
}

/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.number.draft4;

import io.openapiprocessor.jsonschema.schema.JsonInstance;
import io.openapiprocessor.jsonschema.schema.JsonSchema;
import io.openapiprocessor.jsonschema.validator.steps.ValidationStep;

import java.math.BigDecimal;

import static io.openapiprocessor.jsonschema.support.Null.nonNull;

/**
 * validates minimum and exclusiveMinimum. Draft 4.
 */
public class Minimum4 {

    public void validate (JsonSchema schema, JsonInstance instance, ValidationStep parentStep) {
        Number minimum = schema.getMinimum ();
        Boolean exclusive = schema.getExclusiveMinimumB ();

        if (minimum == null)
            return;

        Minimum4Step step = new Minimum4Step (schema, instance);

        boolean valid;
        if (exclusive) {
            valid = compareTo (instance, minimum) > 0;
        } else {
            valid = compareTo (instance, minimum) >= 0;
        }

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

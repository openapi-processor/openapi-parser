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
 * validates maximum and exclusiveMaximum. Draft 4.
 */
public class Maximum4 {

    public void validate (JsonSchema schema, JsonInstance instance, ValidationStep parentStep) {
        Number maximum = schema.getMaximum ();
        Boolean exclusive = schema.getExclusiveMaximumB ();

        if (maximum == null)
            return;

        Maximum4Step step = new Maximum4Step (schema, instance);

        boolean valid;
        if (exclusive) {
            valid = compareTo (instance, maximum) < 0;
        } else {
            valid = compareTo (instance, maximum) <= 0;
        }

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

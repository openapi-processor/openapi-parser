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
 * validates multipleOf. Since Draft 4.
 */
public class MultipleOf {

    public void validate (JsonSchema schema, JsonInstance instance, ValidationStep parentStep) {
        Number multipleOf = schema.getMultipleOf ();
        if (multipleOf == null)
            return;

        MultipleOfStep step = new MultipleOfStep (schema, instance);

        Number instanceValue = getInstanceValue (instance);
        boolean invalid = new BigDecimal (instanceValue.toString ())
            .remainder (new BigDecimal (multipleOf.toString ()))
            .compareTo (BigDecimal.ZERO) != 0;

        if (invalid) {
            step.setInvalid ();
        }

        parentStep.add (step);
    }

    private Number getInstanceValue (JsonInstance instance) {
        return nonNull (instance.asNumber ());
    }
}

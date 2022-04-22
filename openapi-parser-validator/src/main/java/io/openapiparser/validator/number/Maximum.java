/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.number;

import io.openapiparser.schema.*;
import io.openapiparser.validator.steps.NullStep;
import io.openapiparser.validator.steps.ValidationStep;

import java.math.BigDecimal;

/**
 * maximum.
 *
 * <p>See specification:
 * <p>Draft 6:
 * <a href="https://datatracker.ietf.org/doc/html/draft-wright-json-schema-validation-01#section-6.2">
 *     maximum</a>,
 */
public class Maximum {

    public ValidationStep validate (JsonSchema schema, JsonInstance instance) {
        Number maximum = schema.getMaximum ();

        if (maximum == null)
            return new NullStep ();

        MaximumStep step = new MaximumStep (schema, instance);

        boolean valid = compareTo (instance, maximum) <= 0;
        if (!valid) {
            step.setInvalid ();
        }

        return step;
    }

    private int compareTo (JsonInstance instance, Number maximum) {
        return new BigDecimal (instance.asNumber ().toString ())
            .compareTo (new BigDecimal (maximum.toString ()));
    }
}

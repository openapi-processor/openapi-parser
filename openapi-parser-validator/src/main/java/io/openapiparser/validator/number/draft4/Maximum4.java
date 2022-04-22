/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.number.draft4;

import io.openapiparser.schema.*;
import io.openapiparser.validator.steps.*;

import java.math.BigDecimal;

/**
 * validates maximum and exclusiveMaximum.
 *
 * <p>See specification:
 * <p>Draft 4:
 * <a href="https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-5.1.2">
 *     maximum and exclusiveMaximum</a>
 */
public class Maximum4 {

    public ValidationStep validate (JsonSchema schema, JsonInstance instance) {
        Number maximum = schema.getMaximum ();
        Boolean exclusive = schema.getExclusiveMaximumB ();

        if (maximum == null)
            return new NullStep ();

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

        return step;
    }

    private int compareTo (JsonInstance instance, Number maximum) {
        return new BigDecimal (instance.asNumber ().toString ())
            .compareTo (new BigDecimal (maximum.toString ()));
    }
}

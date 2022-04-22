/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.number.draft4;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.steps.NullStep;
import io.openapiparser.validator.steps.ValidationStep;

import java.math.BigDecimal;

/**
 * validates minimum and exclusiveMinimum.
 *
 * <p>See specification:
 * <p>Draft 4:
 * <a href="https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-5.1.3">
 *   minimum and exclusiveMinimum
 * </a>
 */
public class Minimum4 {

    public ValidationStep validate (JsonSchema schema, JsonInstance instance) {
        Number minimum = schema.getMinimum ();
        Boolean exclusive = schema.getExclusiveMinimumB ();

        if (minimum == null)
            return new NullStep ();

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

        return step;
    }

    private int compareTo (JsonInstance instance, Number minimum) {
        return new BigDecimal (instance.asNumber ().toString ())
            .compareTo (new BigDecimal (minimum.toString ()));
    }
}

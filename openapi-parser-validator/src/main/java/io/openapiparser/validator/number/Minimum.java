/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.number;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.steps.NullStep;
import io.openapiparser.validator.steps.ValidationStep;

import java.math.BigDecimal;

import static io.openapiparser.support.Nullness.nonNull;

/**
 * validates minimum.
 *
 * <p>See specification:
 * <p>Draft 6:
 * <a href="https://datatracker.ietf.org/doc/html/draft-wright-json-schema-validation-01#section-6.4">
 *     minimum</a>
 */
public class Minimum {

    public ValidationStep validate (JsonSchema schema, JsonInstance instance) {
        Number minimum = schema.getMinimum ();

        if (minimum == null)
            return new NullStep ("minimum");

        MinimumStep step = new MinimumStep (schema, instance);

        boolean valid = compareTo (instance, minimum) >= 0;
        if (!valid) {
            step.setInvalid ();
        }

        return step;
    }

    private int compareTo (JsonInstance instance, Number minimum) {
        return new BigDecimal (getInstanceValue (instance).toString ())
            .compareTo (new BigDecimal (minimum.toString ()));
    }

    private Number getInstanceValue (JsonInstance instance) {
        return nonNull (instance.asNumber ());
    }
}

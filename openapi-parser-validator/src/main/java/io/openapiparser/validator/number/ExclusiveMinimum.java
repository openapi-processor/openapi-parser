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
 * validates exclusiveMinimum.
 *
 * <p>See specification:
 * <p>Draft 6:
 * <a href="https://datatracker.ietf.org/doc/html/draft-wright-json-schema-validation-01#section-6.5">
 *     exclusiveMinimum</a>
 */
public class ExclusiveMinimum {

    public ValidationStep validate (JsonSchema schema, JsonInstance instance) {
        Number minimum = schema.getExclusiveMinimum ();

        if (minimum == null)
            return new NullStep ();

        ExclusiveMinimumStep step = new ExclusiveMinimumStep (schema, instance);

        boolean valid = compareTo (instance, minimum) > 0;
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

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
 * validates multipleOf.
 *
 * <p>See specification:
 * <p>Draft 6:
 * <a href="https://datatracker.ietf.org/doc/html/draft-wright-json-schema-validation-01#section-6.1">
 *     multipleOf</a>,
 * <br>Draft 4:
 * <a href="https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-5.1.1">
 *     multipleOf</a>
 */
public class MultipleOf {

    public ValidationStep validate (JsonSchema schema, JsonInstance instance) {
        Number multipleOf = schema.getMultipleOf ();
        if (multipleOf == null)
            return new NullStep ();

        MultipleOfStep step = new MultipleOfStep (schema, instance);

        Number instanceValue = getInstanceValue (instance);
        boolean invalid = new BigDecimal (instanceValue.toString ())
            .remainder (new BigDecimal (multipleOf.toString ()))
            .compareTo (BigDecimal.ZERO) != 0;

        if (invalid) {
            step.setInvalid ();
        }

        return step;
    }

    private Number getInstanceValue (JsonInstance instance) {
        return nonNull (instance.asNumber ());
    }
}

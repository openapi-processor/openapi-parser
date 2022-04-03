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

/**
 * validates multipleOf.
 *
 * <p>See specification:
 * <a href="https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-5.1.1">
 *     Draft 4: multipleOf
 * </a>
 */
public class MultipleOf {

    public ValidationStep validate (JsonSchema schema, JsonInstance instance) {
        Number multipleOf = schema.getMultipleOf ();
        if (multipleOf == null)
            return new NullStep ();

        Number instanceValue = instance.asNumber();
        boolean invalid = new BigDecimal (instanceValue.toString ())
            .remainder (new BigDecimal (multipleOf.toString ()))
            .compareTo (BigDecimal.ZERO) != 0;

        if (invalid) {
            return new MultipleOfStep (new MultipleOfError (instance.getPath (), multipleOf));
        }

        return new MultipleOfStep ();
    }
}

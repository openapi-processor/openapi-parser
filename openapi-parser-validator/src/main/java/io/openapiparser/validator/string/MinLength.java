/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.string;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.steps.ValidationStep;

/**
 * validates minLength.
 *
 * <p>See specification:
 * <p>Draft 6:
 * <a href="https://datatracker.ietf.org/doc/html/draft-wright-json-schema-validation-01#section-6.7">
 *     minLength</a>
 * <br>Draft 4:
 * <a href="https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-5.2.2">
 *     minLength</a>
 */
public class MinLength {

    public ValidationStep validate (JsonSchema schema, JsonInstance instance) {
        MinLengthStep step = new MinLengthStep (schema, instance);

        Integer minLength = schema.getMinLength ();
        String instanceValue = instance.asString ();
        boolean valid = instanceValue.codePointCount (0, instanceValue.length ()) >= minLength;
        if (!valid) {
            step.setInvalid ();
        }

        return step;
    }
}

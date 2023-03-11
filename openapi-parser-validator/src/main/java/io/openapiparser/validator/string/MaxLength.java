/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.string;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.steps.NullStep;
import io.openapiparser.validator.steps.ValidationStep;

import static io.openapiparser.support.Nullness.nonNull;

/**
 * validates maxLength.
 *
 * <p>See specification:
 * <p>Draft 6:
 * <a href="https://datatracker.ietf.org/doc/html/draft-wright-json-schema-validation-01#section-6.6">
 *     maxLength</a>
 * <br>Draft 4:
 * <a href="https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-5.2.1">
 *     maxLength</a>
 */
public class MaxLength {

    public ValidationStep validate (JsonSchema schema, JsonInstance instance) {
        Integer maxLength = schema.getMaxLength ();
        if (maxLength == null)
            return new NullStep ("maxLength");

        MaxLengthStep step = new MaxLengthStep (schema, instance);

        String instanceValue = getInstanceValue (instance);
        boolean valid = instanceValue.codePointCount (0, instanceValue.length ()) <= maxLength;
        if (!valid) {
            step.setInvalid ();
        }

        return step;
    }

    private String getInstanceValue (JsonInstance instance) {
        return nonNull (instance.asString ());
    }
}

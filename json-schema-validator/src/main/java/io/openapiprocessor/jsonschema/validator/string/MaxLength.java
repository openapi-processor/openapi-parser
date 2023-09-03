/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.string;

import io.openapiprocessor.jsonschema.schema.JsonInstance;
import io.openapiprocessor.jsonschema.schema.JsonSchema;
import io.openapiprocessor.jsonschema.validator.steps.ValidationStep;

import static io.openapiprocessor.jsonschema.support.Null.nonNull;

/**
 * validates maxLength. Since Draft 4.
 */
public class MaxLength {

    public void validate (JsonSchema schema, JsonInstance instance, ValidationStep parentStep) {
        Integer maxLength = schema.getMaxLength ();
        if (maxLength == null)
            return;

        MaxLengthStep step = new MaxLengthStep (schema, instance);

        String instanceValue = getInstanceValue (instance);
        boolean valid = instanceValue.codePointCount (0, instanceValue.length ()) <= maxLength;
        if (!valid) {
            step.setInvalid ();
        }

        parentStep.add (step);
    }

    private String getInstanceValue (JsonInstance instance) {
        return nonNull (instance.asString ());
    }
}

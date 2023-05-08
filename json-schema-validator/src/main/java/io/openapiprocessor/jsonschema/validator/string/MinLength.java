/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.string;

import io.openapiprocessor.jsonschema.schema.JsonInstance;
import io.openapiprocessor.jsonschema.schema.JsonSchema;
import io.openapiprocessor.jsonschema.validator.steps.ValidationStep;

import static io.openapiprocessor.jsonschema.support.Nullness.nonNull;

/**
 * validates minLength. Since Draft 4.
 */
public class MinLength {

    public void validate (JsonSchema schema, JsonInstance instance, ValidationStep parentStep) {
        Integer minLength = schema.getMinLength ();
        if (minLength == null)
            return;

        MinLengthStep step = new MinLengthStep (schema, instance);
        String instanceValue = getInstanceValue (instance);

        boolean valid = instanceValue.codePointCount (0, instanceValue.length ()) >= minLength;
        if (!valid) {
            step.setInvalid ();
        }

        parentStep.add (step);
    }

    private String getInstanceValue (JsonInstance instance) {
        return nonNull (instance.asString ());
    }
}

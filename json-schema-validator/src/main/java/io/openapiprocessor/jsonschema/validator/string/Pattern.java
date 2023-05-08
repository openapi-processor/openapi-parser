/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.string;

import io.openapiprocessor.jsonschema.schema.JsonInstance;
import io.openapiprocessor.jsonschema.schema.JsonSchema;
import io.openapiprocessor.jsonschema.validator.steps.ValidationStep;

import java.util.regex.Matcher;

import static io.openapiprocessor.jsonschema.support.Nullness.nonNull;

/**
 * validates pattern. Since Draft 4.
 */
public class Pattern {

    public void validate (JsonSchema schema, JsonInstance instance, ValidationStep parentStep) {
        String pattern = schema.getPattern ();
        if (pattern == null)
            return;

        PatternStep step = new PatternStep (schema, instance);

        java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
        String instanceValue = getInstanceValue (instance);
        Matcher m = p.matcher(instanceValue);
        boolean valid = m.find ();
        if(!valid) {
            step.setInvalid ();
        }

        parentStep.add (step);
    }

    private String getInstanceValue (JsonInstance instance) {
        return nonNull (instance.asString ());
    }
}

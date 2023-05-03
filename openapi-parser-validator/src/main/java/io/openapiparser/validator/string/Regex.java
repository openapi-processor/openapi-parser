/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.string;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.steps.ValidationStep;

import static io.openapiparser.support.Nullness.nonNull;

/**
 * validates regex format. Since Draft 7.
 */
public class Regex {

    public void validate (JsonSchema schema, JsonInstance instance, ValidationStep parentStep) {
        String pattern = schema.getPattern ();
        if (pattern == null)
            return;

        RegexStep step = new RegexStep (schema, instance);

        try {
            java.util.regex.Pattern.compile(getInstanceValue (instance));

        } catch (Exception ex) {
            step.setInvalid ();
        }

        parentStep.add (step);
    }

    private String getInstanceValue (JsonInstance instance) {
        return nonNull (instance.asString ());
    }
}

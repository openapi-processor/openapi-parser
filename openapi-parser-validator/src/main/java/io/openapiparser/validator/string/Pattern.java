/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.string;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.steps.NullStep;
import io.openapiparser.validator.steps.ValidationStep;

import java.util.regex.Matcher;

/**
 * validates pattern.
 *
 * <p>See specification:
 * <a href="https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-5.2.3">
 *     Draft 4: pattern
 * </a>
 */
public class Pattern {

    public ValidationStep validate (JsonSchema schema, JsonInstance instance) {
        String pattern = schema.getPattern ();
        if (pattern == null)
            return new NullStep ();

        PatternStep step = new PatternStep (schema, instance);

        java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
        String instanceValue = instance.asString ();
        Matcher m = p.matcher(instanceValue);
        boolean valid = m.find ();
        if(!valid) {
            step.setInvalid ();
        }

        return step;
    }
}

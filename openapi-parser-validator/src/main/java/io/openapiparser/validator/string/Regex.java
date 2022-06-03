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
 * validates regex format.
 *
 * <p>See specification:
 * <p>Draft 7:
 * <a href="https://datatracker.ietf.org/doc/html/draft-handrews-json-schema-validation-01#section-7.3.8">
 *     regex</a>
 */
public class Regex {

    public ValidationStep validate (JsonSchema schema, JsonInstance instance) {
        String pattern = schema.getFormat ();
        if (pattern == null)
            return new NullStep ();

        RegexStep step = new RegexStep (schema, instance);

        try {
            java.util.regex.Pattern.compile(getInstanceValue (instance));

        } catch (Exception ex) {
            step.setInvalid ();
        }

        return step;
    }

    private String getInstanceValue (JsonInstance instance) {
        return nonNull (instance.asString ());
    }
}

/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.string;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.ValidatorSettings;
import io.openapiparser.validator.steps.ValidationStep;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.regex.Pattern;

import static io.openapiparser.schema.Format.REGEX;
import static io.openapiparser.support.Nullness.nonNull;

/**
 * validates regex format. Since Draft 7.
 */
public class Regex {
    private final ValidatorSettings settings;

    public Regex (ValidatorSettings settings) {
        this.settings = settings;
    }

    public void validate (JsonSchema schema, JsonInstance instance, ValidationStep parentStep) {
        String format = schema.getFormat ();
        if (!shouldValidate (format))
            return;

        RegexStep step = new RegexStep (schema, instance);

        try {
            Pattern.compile (getInstanceValue (instance), Pattern.UNICODE_CHARACTER_CLASS);

        } catch (Exception ex) {
            step.setInvalid ();
        }

        parentStep.add (step);
    }

    private boolean shouldValidate (@Nullable String format) {
        return format != null
            && format.equals (REGEX.getFormat ())
            && settings.validateFormat (REGEX);
    }

    private String getInstanceValue (JsonInstance instance) {
        return nonNull (instance.asString ());
    }
}

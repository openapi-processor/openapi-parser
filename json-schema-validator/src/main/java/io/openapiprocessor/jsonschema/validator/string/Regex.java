/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.string;

import io.openapiprocessor.jsonschema.schema.JsonInstance;
import io.openapiprocessor.jsonschema.schema.JsonSchema;
import io.openapiprocessor.jsonschema.validator.ValidatorSettings;
import io.openapiprocessor.jsonschema.validator.steps.ValidationStep;
import io.openapiprocessor.jsonschema.schema.Format;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.regex.Pattern;

import static io.openapiprocessor.jsonschema.support.Null.nonNull;

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
            && format.equals (Format.REGEX.getFormat ())
            && settings.validateFormat (Format.REGEX);
    }

    private String getInstanceValue (JsonInstance instance) {
        return nonNull (instance.asString ());
    }
}

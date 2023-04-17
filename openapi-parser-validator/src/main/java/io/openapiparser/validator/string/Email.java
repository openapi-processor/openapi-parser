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

import java.util.regex.Matcher;

import static io.openapiparser.schema.Format.EMAIL;
import static io.openapiparser.support.Nullness.nonNull;

/**
 * validates email. Since Draft 4.
 *
 * todo replace regex with validation code
 */
public class Email {
    // https://stackoverflow.com/a/48725527
    private static final String EMAIL_REGEX =
        "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*" +
        "@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$";

    private final ValidatorSettings settings;

    public Email (ValidatorSettings settings) {
        this.settings = settings;
    }

    public void validate (JsonSchema schema, JsonInstance instance, ValidationStep parentStep) {
        String format = schema.getFormat ();
        if (!shouldValidate (format))
            return;

        EmailStep step = new EmailStep (schema, instance);

        java.util.regex.Pattern p = java.util.regex.Pattern.compile(EMAIL_REGEX);
        String instanceValue = getInstanceValue (instance);
        Matcher m = p.matcher(instanceValue);
        boolean valid = m.find ();
        if (!valid)
            step.setInvalid ();

        parentStep.add (step);
    }

    private boolean shouldValidate (@Nullable String format) {
        return format != null
            && format.equals (EMAIL.getFormat ())
            && settings.validateFormat (EMAIL);
    }

    private String getInstanceValue (JsonInstance instance) {
        return nonNull (instance.asString ());
    }
}

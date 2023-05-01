/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.string;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.ValidatorSettings;
import io.openapiparser.validator.steps.ValidationStep;
import io.openapiparser.validator.support.EmailValidator;
import org.checkerframework.checker.nullness.qual.Nullable;

import static io.openapiparser.schema.Format.EMAIL;
import static io.openapiparser.support.Nullness.nonNull;

/**
 * validates email. Since Draft 4.
 */
public class Email {

    private final ValidatorSettings settings;

    public Email (ValidatorSettings settings) {
        this.settings = settings;
    }

    public void validate (JsonSchema schema, JsonInstance instance, ValidationStep parentStep) {
        String format = schema.getFormat ();
        if (!shouldValidate (format))
            return;

        EmailStep step = new EmailStep (schema, instance);

        String instanceValue = getInstanceValue (instance);
        boolean valid = new EmailValidator (instanceValue).validate ();

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

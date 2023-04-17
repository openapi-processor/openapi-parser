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

import static io.openapiparser.schema.Format.HOSTNAME;
import static io.openapiparser.support.Nullness.nonNull;

/**
 * validates hostname. Since Draft4.
 */
public class Hostname {
    private final ValidatorSettings settings;

    public Hostname (ValidatorSettings settings) {
        this.settings = settings;
    }

    public void validate (JsonSchema schema, JsonInstance instance, ValidationStep parentStep) {
        String format = schema.getFormat ();
        if (!shouldValidate (format))
            return;

        HostnameStep step = new HostnameStep (schema, instance);

        String instanceValue = getInstanceValue (instance);
        boolean valid = isValid (instanceValue);
        if (!valid) {
            step.setInvalid ();
        }

        parentStep.add (step);
    }

    boolean shouldValidate (@Nullable String format) {
        return format != null
            && format.equals (HOSTNAME.getFormat ())
            && settings.validateFormat (HOSTNAME);
    }

    private String getInstanceValue (JsonInstance instance) {
        return nonNull (instance.asString ());
    }

    private boolean isValid(String ip) {
        String[] labels = ip.split ("\\.");
        if (labels.length == 0) {
            return false;
        }

        for (String value : labels) {
            if (!value.codePoints ().allMatch (this::isValidChar)) {
                return false;
            }

            if (value.startsWith ("-") || value.endsWith ("-")) {
                return false;
            }

            if (value.length () > 63) {
                return false;
            }
        }

        return true;
    }

    private boolean isValidChar (int c) {
        return isNumber (c) || isLetter (c) || isDash (c);
    }

    private boolean isNumber (int c) {
        return c >= 48 && c <= 57;
    }

    private boolean isLetter (int c) {
        return (c >= 65 && c <= 90) || (c >= 97 && c <= 122);
    }

    private boolean isDash (int c) {
        return c == 45;
    }
}

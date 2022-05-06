/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.string;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.ValidatorSettings;
import io.openapiparser.validator.steps.NullStep;
import io.openapiparser.validator.steps.ValidationStep;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * validates hostname.
 *
 * <p>See specification:
 *
 * <p>Draft 6:
 * <a href="https://datatracker.ietf.org/doc/html/draft-wright-json-schema-validation-01#section-8.3.3">
 *     hostname</a>
 *
 * <br>Draft 4:
 * <a href="https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-7.3.3">
 *     hostname</a>
 */
public class Hostname {
    private final ValidatorSettings settings;

    public Hostname (ValidatorSettings settings) {
        this.settings = settings;
    }

    public ValidationStep validate (JsonSchema schema, JsonInstance instance) {
        String format = schema.getFormat ();
        if (!shouldValidate (format))
            return new NullStep ();

        HostnameStep step = new HostnameStep (schema, instance);

        String instanceValue = instance.asString ();
        boolean valid = isValid (instanceValue);
        if (!valid) {
            step.setInvalid ();
        }

        return step;
    }

    boolean shouldValidate (@Nullable String format) {
        return format != null
            && format.equals ("hostname")
            && settings.validateFormat ("hostname");
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

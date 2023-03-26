/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.string;

import io.openapiparser.schema.*;
import io.openapiparser.validator.ValidatorSettings;
import io.openapiparser.validator.steps.*;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.UUID;

import static io.openapiparser.support.Nullness.nonNull;

public class Uuid {

    private final ValidatorSettings settings;

    public Uuid (ValidatorSettings settings) {
        this.settings = settings;
    }

    public ValidationStep validate (JsonSchema schema, JsonInstance instance) {
        String format = schema.getFormat ();
        if (!shouldValidate (format))
            return new NullStep ("format");

        UuidStep step = new UuidStep (schema, instance);

        String instanceValue = getInstanceValue (instance);
        boolean valid = isUuid (instanceValue);
        if (!valid) {
            step.setInvalid ();
        }

        return step;
    }

    private boolean shouldValidate (@Nullable String format) {
        return format != null
            && format.equals (Format.UUID.getFormat ())
            && settings.validateFormat (Format.UUID);
    }

    private String getInstanceValue (JsonInstance instance) {
        return nonNull (instance.asString ());
    }

    @SuppressWarnings ("ResultOfMethodCallIgnored")
    private boolean isUuid (String uuid) {
        try {
            UUID.fromString (uuid);

            // UUID doesn't check min size
            return uuid.length () == 36;

        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}

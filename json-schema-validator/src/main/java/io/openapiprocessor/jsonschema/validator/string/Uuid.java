/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.string;

import io.openapiprocessor.jsonschema.schema.Format;
import io.openapiprocessor.jsonschema.schema.JsonInstance;
import io.openapiprocessor.jsonschema.schema.JsonSchema;
import io.openapiprocessor.jsonschema.validator.ValidatorSettings;
import io.openapiprocessor.jsonschema.validator.steps.ValidationStep;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.UUID;

import static io.openapiprocessor.jsonschema.support.Nullness.nonNull;

public class Uuid {

    private final ValidatorSettings settings;

    public Uuid (ValidatorSettings settings) {
        this.settings = settings;
    }

    public void validate (JsonSchema schema, JsonInstance instance, ValidationStep parentStep) {
        String format = schema.getFormat ();
        if (!shouldValidate (format))
            return;

        UuidStep step = new UuidStep (schema, instance);

        String instanceValue = getInstanceValue (instance);
        boolean valid = isUuid (instanceValue);
        if (!valid) {
            step.setInvalid ();
        }

        parentStep.add (step);
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

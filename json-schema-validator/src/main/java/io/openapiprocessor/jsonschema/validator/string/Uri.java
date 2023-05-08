/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.validator.string;

import io.openapiprocessor.jsonschema.schema.JsonInstance;
import io.openapiprocessor.jsonschema.schema.JsonSchema;
import io.openapiprocessor.jsonschema.validator.ValidatorSettings;
import io.openapiprocessor.jsonschema.validator.steps.ValidationStep;
import io.openapiprocessor.jsonschema.validator.support.UriValidator;
import io.openapiprocessor.jsonschema.schema.Format;
import org.checkerframework.checker.nullness.qual.Nullable;

import static io.openapiprocessor.jsonschema.support.Nullness.nonNull;

/**
 * validates format: uri. Since Draft 4.
 */
public class Uri {
    private final ValidatorSettings settings;

    public Uri (ValidatorSettings settings) {
        this.settings = settings;
    }

    public void validate (JsonSchema schema, JsonInstance instance, ValidationStep parentStep) {
        String format = schema.getFormat ();
        if (!shouldValidate (format))
            return;

        UriStep step = new UriStep (schema, instance);

        String instanceValue = getInstanceValue (instance);
        boolean valid = new UriValidator (instanceValue).validateAbsolute ();
        if (!valid) {
            step.setInvalid ();
        }

        parentStep.add (step);
    }

    private boolean shouldValidate (@Nullable String format) {
        return format != null
            && format.equals (Format.URI.getFormat ())
            && settings.validateFormat (Format.URI);
    }

    private String getInstanceValue (JsonInstance instance) {
        return nonNull (instance.asString ());
    }
}

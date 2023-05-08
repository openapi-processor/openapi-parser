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
 * validates format: uri-reference. Since Draft 6.
 */
public class UriReference {
    private final ValidatorSettings settings;

    public UriReference (ValidatorSettings settings) {
        this.settings = settings;
    }

    public void validate (JsonSchema schema, JsonInstance instance, ValidationStep parentStep) {
        String format = schema.getFormat ();
        if (!shouldValidate (format))
            return;

        UriStep step = new UriStep (schema, instance);

        String instanceValue = getInstanceValue (instance);
        boolean valid = new UriValidator (instanceValue).validate ();
        if (!valid) {
            step.setInvalid ();
        }

        parentStep.add (step);
    }

    private boolean shouldValidate (@Nullable String format) {
        return format != null
            && format.equals (Format.URI_REFERENCE.getFormat ())
            && settings.validateFormat (Format.URI_REFERENCE);
    }

    private String getInstanceValue (JsonInstance instance) {
        return nonNull (instance.asString ());
    }
}
